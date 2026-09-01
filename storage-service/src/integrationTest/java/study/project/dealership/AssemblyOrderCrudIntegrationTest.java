package study.project.dealership;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.application.services.AssemblyOrderServiceImpl;
import study.project.dealership.common.messaging.OrderType;
import study.project.dealership.contracts.assembly.request.RequestCreateAssemblyOrder;
import study.project.dealership.contracts.assembly.request.RequestUpdateAssemblyOrder;
import study.project.dealership.domain.assembly.AssemblyOrderStatus;
import study.project.dealership.support.IntegrationContainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class AssemblyOrderCrudIntegrationTest extends IntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
        registerGrpc(registry);
    }

    @Autowired
    AssemblyOrderServiceImpl assemblyOrderService;

    @Test
    void assemblyOrderCrudLifecycle() {
        UUID sourceOrderId = UUID.randomUUID();
        UUID warehouseAdminId = UUID.randomUUID();

        var created = assemblyOrderService.create(new RequestCreateAssemblyOrder(
                sourceOrderId,
                OrderType.STOCK,
                UUID.randomUUID(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                warehouseAdminId,
                AssemblyOrderStatus.CREATED.name()
        ));

        assertThat(created.sourceOrderId()).isEqualTo(sourceOrderId);
        assertThat(assemblyOrderService.findAll()).anyMatch(dto -> dto.id().equals(created.id()));

        var updated = assemblyOrderService.update(new RequestUpdateAssemblyOrder(
                created.id(),
                warehouseAdminId,
                AssemblyOrderStatus.ASSEMBLED.name()
        ));
        assertThat(updated.status()).isEqualTo(AssemblyOrderStatus.ASSEMBLED.name());

        assemblyOrderService.delete(created.id());
        assertThat(assemblyOrderService.findById(created.id()).removed()).isTrue();
    }
}
