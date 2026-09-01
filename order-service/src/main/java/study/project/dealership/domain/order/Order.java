package study.project.dealership.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import study.project.dealership.domain.BaseEntity;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "order_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Order extends BaseEntity {

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "manager_id")
    private UUID managerId;
}