package study.project.dealership.common.messaging;

public final class KafkaTopics {

    public static final String ORDER_SENT_FOR_APPROVAL = "order.sent-for-approval";
    public static final String ORDER_APPROVED = "order.approved";
    public static final String ORDER_REJECTED = "order.rejected";

    private KafkaTopics() {
    }
}
