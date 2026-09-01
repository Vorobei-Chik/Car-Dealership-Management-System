package study.project.dealership.presentation.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import study.project.dealership.application.OrderDtoFactory;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.mapping.OrderMapper;
import study.project.dealership.contracts.order.OrderService;
import study.project.dealership.contracts.order.model.CustomOrderDTO;
import study.project.dealership.contracts.order.model.OrderDTO;
import study.project.dealership.contracts.order.model.StockOrderDTO;
import study.project.dealership.contracts.order.request.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderDtoFactory orderDtoFactory;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> findOrder(@PathVariable @NotNull UUID id) {
        return ResponseEntity.ok(OrderMapper.toBaseDto(orderService.findOrder(new RequestFindOrder(id))));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeOrder(@PathVariable @NotNull UUID id) {
        orderService.removeOrder(new RequestRemoveOrder(id));
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/custom")
    public ResponseEntity<CustomOrderDTO> createCustomOrder(@Valid @RequestBody RequestCreateCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.createCustomOrder(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/custom")
    public ResponseEntity<List<CustomOrderDTO>> getAllCustomOrders() {
        return ResponseEntity.ok(orderService.getAllCustomOrders().stream().map(orderDtoFactory::toDto).toList());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/custom/approve")
    public ResponseEntity<CustomOrderDTO> approveCustomOrder(@Valid @RequestBody RequestApproveCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.approveCustomOrder(request)));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/custom/await-delivery")
    public ResponseEntity<CustomOrderDTO> awaitDeliveryCustomOrder(@Valid @RequestBody RequestAwaitDeliveryCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.awaitDeliveryCustomOrder(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/custom/await-payment")
    public ResponseEntity<CustomOrderDTO> awaitPaymentCustomOrder(@Valid @RequestBody RequestAwaitPaymentCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.awaitPaymentCustomOrder(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/custom/cancel")
    public ResponseEntity<CustomOrderDTO> cancelCustomOrder(@Valid @RequestBody RequestCancelCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.cancelCustomOrder(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/custom/complete")
    public ResponseEntity<CarDTO> completeCustomOrder(@Valid @RequestBody RequestCompleteCustomOrder request) {
        return ResponseEntity.ok(orderService.completeCustomOrder(request));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/custom/ready-for-pickup")
    public ResponseEntity<CustomOrderDTO> markReadyForPickupCustomOrder(@Valid @RequestBody RequestMarkReadyForPickupCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.markReadyForPickupCustomOrder(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/custom/pay")
    public ResponseEntity<CustomOrderDTO> payCustomOrder(@Valid @RequestBody RequestPayCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.payCustomOrder(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/custom")
    public ResponseEntity<CustomOrderDTO> updateCustomOrder(@Valid @RequestBody RequestUpdateCustomOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.updateCustomOrder(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/stock")
    public ResponseEntity<StockOrderDTO> createStockOrder(@Valid @RequestBody RequestCreateStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.createStockOrder(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stock")
    public ResponseEntity<List<StockOrderDTO>> getAllStockOrders() {
        return ResponseEntity.ok(orderService.getAllStockOrders().stream().map(orderDtoFactory::toDto).toList());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/stock/approve")
    public ResponseEntity<StockOrderDTO> approveStockOrder(@Valid @RequestBody RequestApproveStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.approveStockOrder(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/stock/await-payment")
    public ResponseEntity<StockOrderDTO> awaitPaymentStockOrder(@Valid @RequestBody RequestAwaitPaymentStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.awaitPaymentStockOrder(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/stock/cancel")
    public ResponseEntity<StockOrderDTO> cancelStockOrder(@Valid @RequestBody RequestCancelStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.cancelStockOrder(request)));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/stock/complete")
    public ResponseEntity<CarDTO> completeStockOrder(@Valid @RequestBody RequestCompleteStockOrder request) {
        return ResponseEntity.ok(orderService.completeStockOrder(request));
    }

    @PreAuthorize("hasRole('WAREHOUSE_ADMIN')")
    @PostMapping("/stock/ready-for-pickup")
    public ResponseEntity<StockOrderDTO> markReadyForPickupStockOrder(@Valid @RequestBody RequestMarkReadyForPickupStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.markReadyForPickupStockOrder(request)));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/stock/pay")
    public ResponseEntity<StockOrderDTO> payStockOrder(@Valid @RequestBody RequestPayStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.payStockOrder(request)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/stock")
    public ResponseEntity<StockOrderDTO> updateStockOrder(@Valid @RequestBody RequestUpdateStockOrder request) {
        return ResponseEntity.ok(orderDtoFactory.toDto(orderService.updateStockOrder(request)));
    }
}
