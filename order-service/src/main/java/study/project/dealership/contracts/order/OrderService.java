package study.project.dealership.contracts.order;

import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.order.request.*;
import study.project.dealership.domain.order.CustomOrder;
import study.project.dealership.domain.order.Order;
import study.project.dealership.domain.order.StockOrder;

import java.util.List;

public interface OrderService {
    CustomOrder createCustomOrder(RequestCreateCustomOrder request);

    StockOrder createStockOrder(RequestCreateStockOrder request);

    CustomOrder approveCustomOrder(RequestApproveCustomOrder request);

    CustomOrder awaitDeliveryCustomOrder(RequestAwaitDeliveryCustomOrder request);

    CustomOrder awaitPaymentCustomOrder(RequestAwaitPaymentCustomOrder request);

    CustomOrder cancelCustomOrder(RequestCancelCustomOrder request);

    CarDTO completeCustomOrder(RequestCompleteCustomOrder request);

    List<CustomOrder> getAllCustomOrders();

    CustomOrder markReadyForPickupCustomOrder(RequestMarkReadyForPickupCustomOrder request);

    CustomOrder payCustomOrder(RequestPayCustomOrder request);

    CustomOrder updateCustomOrder(RequestUpdateCustomOrder request);

    StockOrder approveStockOrder(RequestApproveStockOrder request);

    StockOrder awaitPaymentStockOrder(RequestAwaitPaymentStockOrder request);

    StockOrder cancelStockOrder(RequestCancelStockOrder request);

    CarDTO completeStockOrder(RequestCompleteStockOrder request);

    List<StockOrder> getAllStockOrders();

    StockOrder markReadyForPickupStockOrder(RequestMarkReadyForPickupStockOrder request);

    StockOrder payStockOrder(RequestPayStockOrder request);

    StockOrder updateStockOrder(RequestUpdateStockOrder request);

    Order findOrder(RequestFindOrder request);

    void removeOrder(RequestRemoveOrder request);
}
