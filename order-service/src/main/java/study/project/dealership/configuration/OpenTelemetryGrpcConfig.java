package study.project.dealership.configuration;

import io.grpc.ClientInterceptor;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.grpc.v1_6.GrpcTelemetry;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenTelemetryGrpcConfig {

    @Bean
    @GrpcGlobalClientInterceptor
    ClientInterceptor grpcClientTracing(OpenTelemetry openTelemetry) {
        return GrpcTelemetry.create(openTelemetry).newClientInterceptor();
    }
}
