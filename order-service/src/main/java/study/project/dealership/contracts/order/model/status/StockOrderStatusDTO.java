package study.project.dealership.contracts.order.model.status;

public enum StockOrderStatusDTO {
    DRAFT,
    MANAGER_APPROVED,
    AWAITING_PAYMENT,
    PAID,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED
}
