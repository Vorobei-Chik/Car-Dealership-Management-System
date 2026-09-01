package study.project.dealership.abstractions.repository;

import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.Order;
import study.project.dealership.domain.order.StockOrder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    void add(Order order);
    void update(Order order);
    void remove(UUID id);
    Optional<Order> find(UUID id);
    List<Order> getAll();
    List<StockOrder> getStockOrders();
    List<CustomOrder> getCustomOrders();
    List<StockOrder> getStockOrdersByUserId(UUID id);
    List<CustomOrder> getCustomOrdersByUserId(UUID id);
}