package study.project.dealership.application.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.project.dealership.abstractions.repository.OrderRepository;
import study.project.dealership.abstractions.repository.UserRepository;
import study.project.dealership.application.exception.BadRequestException;
import study.project.dealership.application.exception.ConflictException;
import study.project.dealership.application.exception.ForbiddenException;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.common.messaging.KafkaTopics;
import study.project.dealership.common.messaging.OrderSentForApprovalEvent;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.mapping.EnumMapper;
import study.project.dealership.contracts.mapping.ValueObjectMapper;
import study.project.dealership.contracts.order.OrderService;
import study.project.dealership.contracts.order.request.*;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.Order;
import study.project.dealership.domain.order.OrderConfiguration;
import study.project.dealership.domain.order.StockOrder;
import study.project.dealership.domain.valueobject.carinfo.Color;
import study.project.dealership.domain.valueobject.orderinfo.CustomOrderStatus;
import study.project.dealership.domain.valueobject.orderinfo.StockOrderStatus;
import study.project.dealership.infrastructure.client.StorageClient;
import study.project.dealership.infrastructure.messaging.outbox.OutboxService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final StorageClient storageClient;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public CustomOrder createCustomOrder(@NotNull RequestCreateCustomOrder request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID clientId = UUID.fromString(auth.getName());
        UUID managerId = userRepository.getRandomManager()
                .orElseThrow(() -> new NotFoundException("No manager found"))
                .getId();

        storageClient.validateAndReserveCustomOrder(request);

        Color color;
        try {
            color = ValueObjectMapper.toColor(request.color());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid color: " + e.getMessage());
        }

        OrderConfiguration config = new OrderConfiguration();
        config.setModelId(request.carModelId());
        config.setEngineId(request.engineId());
        config.setGearBoxId(request.gearBoxId());
        config.setTransmissionId(request.transmissionId());
        config.setWheelId(request.wheelId());
        config.setInteriorId(request.interiorId());
        config.setRudderId(request.rudderId());
        config.setColor(color);
        config.setStandard(false);

        CustomOrder order = CustomOrder.create(clientId, managerId, config);
        order.setStatus(CustomOrderStatus.DRAFT);
        orderRepository.add(order);
        return order;
    }

    @Override
    @Transactional
    public StockOrder createStockOrder(@NotNull RequestCreateStockOrder request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UUID clientId = UUID.fromString(auth.getName());
        UUID managerId = userRepository.getRandomManager()
                .orElseThrow(() -> new NotFoundException("No manager found"))
                .getId();

        storageClient.getCar(request.carId());
        storageClient.reserveCar(request.carId());

        StockOrder order = StockOrder.create(clientId, managerId, request.carId());
        order.setStatus(StockOrderStatus.DRAFT);
        orderRepository.add(order);
        return order;
    }

    @Override
    @Transactional
    public CustomOrder approveCustomOrder(@NotNull RequestApproveCustomOrder request) {
        UUID managerId = currentUserId();
        CustomOrder order = loadCustomOrder(request.orderId());
        assertManager(order, managerId);
        if (order.getStatus() != CustomOrderStatus.DRAFT) {
            throw new ConflictException("Order is not in DRAFT status");
        }
        order.setStatus(CustomOrderStatus.WAREHOUSE_APPROVED);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public CustomOrder awaitDeliveryCustomOrder(@NotNull RequestAwaitDeliveryCustomOrder request) {
        CustomOrder order = loadCustomOrder(request.orderId());
        if (order.getStatus() != CustomOrderStatus.PAID) {
            throw new ConflictException("Order is not in PAID status");
        }
        order.setStatus(CustomOrderStatus.AWAITING_DELIVERY);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public CustomOrder awaitPaymentCustomOrder(@NotNull RequestAwaitPaymentCustomOrder request) {
        UUID managerId = currentUserId();
        CustomOrder order = loadCustomOrder(request.orderId());
        assertManager(order, managerId);
        if (order.getStatus() != CustomOrderStatus.WAREHOUSE_APPROVED) {
            throw new ConflictException("Order is not in WAREHOUSE_APPROVED status");
        }
        order.setStatus(CustomOrderStatus.AWAITING_PAYMENT);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public CustomOrder cancelCustomOrder(@NotNull RequestCancelCustomOrder request) {
        UUID managerId = currentUserId();
        CustomOrder order = loadCustomOrder(request.orderId());
        assertManager(order, managerId);
        CustomOrderStatus status = order.getStatus();
        if (status != CustomOrderStatus.DRAFT
                && status != CustomOrderStatus.WAREHOUSE_APPROVED
                && status != CustomOrderStatus.AWAITING_PAYMENT) {
            throw new ConflictException("Order cannot be cancelled in current status");
        }
        order.setStatus(CustomOrderStatus.CANCELLED);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public CarDTO completeCustomOrder(@NotNull RequestCompleteCustomOrder request) {
        UUID clientId = currentUserId();
        CustomOrder order = loadCustomOrder(request.orderId());
        if (!order.getClientId().equals(clientId)) {
            throw new ForbiddenException("Order does not belong to this client");
        }
        if (order.getStatus() != CustomOrderStatus.READY_FOR_PICKUP) {
            throw new ConflictException("Order is not ready for pickup");
        }
        OrderConfiguration config = order.getConfiguration();
        CarDTO car = storageClient.buildCarFromConfiguration(
                storageClient.getConfigurationSnapshot(
                        config.getModelId(), config.getEngineId(), config.getGearBoxId(),
                        config.getTransmissionId(), config.getWheelId(), config.getInteriorId(),
                        config.getRudderId(), config.getColor().getValue()
                )
        );
        order.setStatus(CustomOrderStatus.COMPLETED);
        orderRepository.update(order);
        return car;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomOrder> getAllCustomOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isManager(auth)) {
            return orderRepository.getCustomOrders();
        }
        return orderRepository.getCustomOrdersByUserId(UUID.fromString(auth.getName()));
    }

    @Override
    @Transactional
    public CustomOrder markReadyForPickupCustomOrder(@NotNull RequestMarkReadyForPickupCustomOrder request) {
        CustomOrder order = loadCustomOrder(request.orderId());
        if (order.getStatus() != CustomOrderStatus.AWAITING_DELIVERY) {
            throw new ConflictException("Order is not in AWAITING_DELIVERY status");
        }
        order.setStatus(CustomOrderStatus.READY_FOR_PICKUP);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public CustomOrder payCustomOrder(@NotNull RequestPayCustomOrder request) {
        UUID managerId = currentUserId();
        CustomOrder order = loadCustomOrder(request.orderId());
        assertManager(order, managerId);
        if (order.getStatus() != CustomOrderStatus.AWAITING_PAYMENT) {
            throw new ConflictException("Order is not in AWAITING_PAYMENT status");
        }
        order.setStatus(CustomOrderStatus.PAID);
        orderRepository.update(order);
        publishSentForApproval(order);
        return order;
    }

    @Override
    @Transactional
    public CustomOrder updateCustomOrder(@NotNull RequestUpdateCustomOrder request) {
        CustomOrder order = loadCustomOrder(request.id());
        OrderConfiguration config = order.getConfiguration();
        config.setModelId(request.carModelId());
        config.setEngineId(request.engineId());
        config.setGearBoxId(request.gearBoxId());
        config.setTransmissionId(request.transmissionId());
        config.setWheelId(request.wheelId());
        config.setInteriorId(request.interiorId());
        config.setRudderId(request.rudderId());
        try {
            config.setColor(ValueObjectMapper.toColor(request.color()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid color: " + e.getMessage());
        }
        order.setClientId(request.clientId());
        order.setManagerId(request.managerId());
        order.setStatus(EnumMapper.toDomain(request.status()));
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public StockOrder approveStockOrder(@NotNull RequestApproveStockOrder request) {
        UUID managerId = currentUserId();
        StockOrder order = loadStockOrder(request.orderId());
        assertManager(order, managerId);
        if (order.getStatus() != StockOrderStatus.DRAFT) {
            throw new ConflictException("Order is not in DRAFT status");
        }
        order.setStatus(StockOrderStatus.MANAGER_APPROVED);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public StockOrder awaitPaymentStockOrder(@NotNull RequestAwaitPaymentStockOrder request) {
        UUID managerId = currentUserId();
        StockOrder order = loadStockOrder(request.orderId());
        assertManager(order, managerId);
        if (order.getStatus() != StockOrderStatus.MANAGER_APPROVED) {
            throw new ConflictException("Order is not in MANAGER_APPROVED status");
        }
        order.setStatus(StockOrderStatus.AWAITING_PAYMENT);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public StockOrder cancelStockOrder(@NotNull RequestCancelStockOrder request) {
        UUID managerId = currentUserId();
        StockOrder order = loadStockOrder(request.orderId());
        assertManager(order, managerId);
        StockOrderStatus status = order.getStatus();
        if (status != StockOrderStatus.DRAFT
                && status != StockOrderStatus.MANAGER_APPROVED
                && status != StockOrderStatus.AWAITING_PAYMENT) {
            throw new ConflictException("Order cannot be cancelled in current status");
        }
        storageClient.releaseCar(order.getCarId());
        order.setStatus(StockOrderStatus.CANCELLED);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public CarDTO completeStockOrder(@NotNull RequestCompleteStockOrder request) {
        UUID clientId = currentUserId();
        StockOrder order = loadStockOrder(request.orderId());
        if (!order.getClientId().equals(clientId)) {
            throw new ForbiddenException("Order does not belong to this client");
        }
        if (order.getStatus() != StockOrderStatus.READY_FOR_PICKUP) {
            throw new ConflictException("Order is not ready for pickup");
        }
        order.setStatus(StockOrderStatus.COMPLETED);
        orderRepository.update(order);
        return storageClient.getCar(order.getCarId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockOrder> getAllStockOrders() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (isManager(auth)) {
            return orderRepository.getStockOrders();
        }
        return orderRepository.getStockOrdersByUserId(UUID.fromString(auth.getName()));
    }

    @Override
    @Transactional
    public StockOrder markReadyForPickupStockOrder(@NotNull RequestMarkReadyForPickupStockOrder request) {
        StockOrder order = loadStockOrder(request.orderId());
        if (order.getStatus() != StockOrderStatus.PAID) {
            throw new ConflictException("Order is not in PAID status");
        }
        order.setStatus(StockOrderStatus.READY_FOR_PICKUP);
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional
    public StockOrder payStockOrder(@NotNull RequestPayStockOrder request) {
        UUID managerId = currentUserId();
        StockOrder order = loadStockOrder(request.orderId());
        assertManager(order, managerId);
        if (order.getStatus() != StockOrderStatus.AWAITING_PAYMENT) {
            throw new ConflictException("Order is not in AWAITING_PAYMENT status");
        }
        order.setStatus(StockOrderStatus.PAID);
        orderRepository.update(order);
        publishSentForApproval(order);
        return order;
    }

    @Override
    @Transactional
    public StockOrder updateStockOrder(@NotNull RequestUpdateStockOrder request) {
        StockOrder order = loadStockOrder(request.id());
        storageClient.getCar(request.carId());
        order.setCarId(request.carId());
        order.setClientId(order.getClientId());
        order.setManagerId(order.getManagerId());
        order.setStatus(EnumMapper.toDomain(request.status()));
        orderRepository.update(order);
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Order findOrder(RequestFindOrder request) {
        return orderRepository.find(request.id())
                .orElseThrow(() -> new NotFoundException("Order not found"));
    }

    @Override
    @Transactional
    public void removeOrder(RequestRemoveOrder request) {
        if (orderRepository.find(request.id()).isEmpty()) {
            throw new NotFoundException("Order not found");
        }
        orderRepository.remove(request.id());
    }

    private void publishSentForApproval(CustomOrder order) {
        UUID traceId = UUID.randomUUID();
        OrderConfiguration config = order.getConfiguration();
        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                order.getId(),
                OrderType.CUSTOM,
                traceId,
                null,
                config.getModelId(),
                config.getEngineId(),
                config.getGearBoxId(),
                config.getTransmissionId(),
                config.getWheelId(),
                config.getInteriorId(),
                config.getRudderId()
        );
        outboxService.enqueue(
                KafkaTopics.ORDER_SENT_FOR_APPROVAL,
                order.getId().toString(),
                event,
                traceId
        );
    }

    private void publishSentForApproval(StockOrder order) {
        UUID traceId = UUID.randomUUID();
        OrderSentForApprovalEvent event = new OrderSentForApprovalEvent(
                order.getId(),
                OrderType.STOCK,
                traceId,
                order.getCarId(),
                null, null, null, null, null, null, null
        );
        outboxService.enqueue(
                KafkaTopics.ORDER_SENT_FOR_APPROVAL,
                order.getId().toString(),
                event,
                traceId
        );
    }

    private CustomOrder loadCustomOrder(UUID orderId) {
        Order order = orderRepository.find(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (!(order instanceof CustomOrder customOrder)) {
            throw new NotFoundException("Order not found");
        }
        return customOrder;
    }

    private StockOrder loadStockOrder(UUID orderId) {
        Order order = orderRepository.find(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if (!(order instanceof StockOrder stockOrder)) {
            throw new NotFoundException("Order not found");
        }
        return stockOrder;
    }

    private static UUID currentUserId() {
        return UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private static void assertManager(Order order, UUID managerId) {
        if (!order.getManagerId().equals(managerId)) {
            throw new ForbiddenException("Does not have access to this order");
        }
    }

    private static boolean isManager(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER"));
    }
}
