package study.project.dealership.domain.part;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import study.project.dealership.domain.BaseEntity;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "part_compatibilities")
public class PartCompatibility extends BaseEntity {
    @Column(name = "model_id", nullable = false)
    private UUID modelId;

    @Column(name = "part_id", nullable = false)
    private UUID partId;
}
