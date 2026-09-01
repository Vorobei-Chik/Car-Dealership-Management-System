package study.project.dealership;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import study.project.dealership.support.OrderIntegrationContainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class OrderServiceIntegrationTest extends OrderIntegrationContainers {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registerDataSource(registry);
        registerKafka(registry);
    }

    @Test
    void contextLoadsWithTestcontainers() {
        assertThat(POSTGRES.isRunning()).isTrue();
        assertThat(KAFKA.isRunning()).isTrue();
    }
}
