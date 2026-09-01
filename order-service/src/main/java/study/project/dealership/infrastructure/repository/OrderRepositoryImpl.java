package study.project.dealership.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.project.dealership.abstractions.repository.OrderRepository;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.Order;
import study.project.dealership.domain.order.StockOrder;
import study.project.dealership.infrastructure.database.repository.CustomOrderJpaRepository;
import study.project.dealership.infrastructure.database.repository.OrderJpaRepository;
import study.project.dealership.infrastructure.database.repository.StockOrderJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final CustomOrderJpaRepository customOrderJpaRepository;
    private final StockOrderJpaRepository stockOrderJpaRepository;

    @Override
    public void add(Order order) {
        orderJpaRepository.save(order);
    }

    @Override
    public void update(Order order) {
        orderJpaRepository.save(order);
    }

    @Override
    public void remove(UUID id) {
        orderJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Order> find(UUID id) {
        return orderJpaRepository.findById(id);
    }

    @Override
    public List<Order> getAll() {
        return orderJpaRepository.findAll();
    }

    @Override
    public List<StockOrder> getStockOrders() {
        return stockOrderJpaRepository.findAll();
    }

    @Override
    public List<CustomOrder> getCustomOrders() {
        return customOrderJpaRepository.findAll();
    }

    @Override
    public List<StockOrder> getStockOrdersByUserId(UUID id) {
        return stockOrderJpaRepository.findAllByClientId(id);
    }

    @Override
    public List<CustomOrder> getCustomOrdersByUserId(UUID id) {
        return customOrderJpaRepository.findAllByClientId(id);
    }
}