package study.project.dealership.configuration;

import io.grpc.ServerInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryGrpcConfig {

    @Bean
    @GrpcGlobalServerInterceptor
    ServerInterceptor grpcServerTracing(OpenTelemetry openTelemetry) {
        return GrpcTelemetry.create(openTelemetry).newServerInterceptor();
    }
}
