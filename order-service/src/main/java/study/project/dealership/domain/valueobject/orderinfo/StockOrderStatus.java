package study.project.dealership.domain.valueobject.orderinfo;

public enum StockOrderStatus {
    DRAFT,
    MANAGER_APPROVED,
    AWAITING_PAYMENT,
    PAID,
    READY_FOR_PICKUP,
    COMPLETED,
    CANCELLED
}
