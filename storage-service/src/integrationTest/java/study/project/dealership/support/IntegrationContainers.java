package study.project.dealership.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.kafka.ConfluentKafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class IntegrationContainers {

    private static final AtomicInteger GRPC_IN_PROCESS_ID = new AtomicInteger();

    @Container
    @SuppressWarnings("resource")
    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"))
            .withDatabaseName("dealership_storage")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    protected static final ConfluentKafkaContainer KAFKA = new ConfluentKafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.6.1"));

    protected static void registerDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    protected static void registerKafka(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }

    protected static void registerGrpc(DynamicPropertyRegistry registry) {
        String inProcessName = "storage-grpc-it-" + GRPC_IN_PROCESS_ID.incrementAndGet();
        registry.add("grpc.server.in-process-name", () -> inProcessName);
        registry.add("grpc.client.storage-grpc-it.address", () -> "in-process:" + inProcessName);
    }
}
