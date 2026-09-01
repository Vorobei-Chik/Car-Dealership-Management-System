package study.project.dealership.infrastructure.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import study.project.dealership.application.exception.NotFoundException;
import study.project.dealership.contracts.car.model.CarDTO;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.contracts.order.request.RequestCreateCustomOrder;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StorageClient {

    private final RestClient.Builder restClientBuilder;

    @Value("${app.storage-service.base-url}")
    private String storageBaseUrl;

    public CarDTO getCar(UUID carId) {
        return restClient().get()
                .uri("/api/cars/{id}", carId)
                .retrieve()
                .onStatus(status -> status.value() == HttpStatus.NOT_FOUND.value(),
                        (request, response) -> {
                            throw new NotFoundException("Car not found");
                        })
                .body(CarDTO.class);
    }

    public void reserveCar(UUID carId) {
        restClient().post()
                .uri("/api/internal/cars/{id}/reserve", carId)
                .retrieve()
                .toBodilessEntity();
    }

    public void releaseCar(UUID carId) {
        restClient().post()
                .uri("/api/internal/cars/{id}/release", carId)
                .retrieve()
                .toBodilessEntity();
    }

    public void validateAndReserveCustomOrder(RequestCreateCustomOrder request) {
        restClient().post()
                .uri("/api/internal/orders/custom/reserve")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }

    public void releaseCustomOrderParts(UUID modelId, UUID engineId, UUID gearBoxId,
                                        UUID transmissionId, UUID wheelId, UUID interiorId, UUID rudderId) {
        restClient().post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/internal/orders/custom/release")
                        .queryParam("modelId", modelId)
                        .queryParam("engineId", engineId)
                        .queryParam("gearBoxId", gearBoxId)
                        .queryParam("transmissionId", transmissionId)
                        .queryParam("wheelId", wheelId)
                        .queryParam("interiorId", interiorId)
                        .queryParam("rudderId", rudderId)
                        .build())
                .retrieve()
                .toBodilessEntity();
    }

    public CarDTO buildCarFromConfiguration(ConfigurationDTO configuration) {
        return restClient().post()
                .uri("/api/internal/cars/build")
                .contentType(MediaType.APPLICATION_JSON)
                .body(configuration)
                .retrieve()
                .body(CarDTO.class);
    }

    public ConfigurationDTO getConfigurationSnapshot(UUID modelId, UUID engineId, UUID gearBoxId,
                                                     UUID transmissionId, UUID wheelId, UUID interiorId,
                                                     UUID rudderId, String colorHex) {
        return restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/internal/configurations/snapshot")
                        .queryParam("modelId", modelId)
                        .queryParam("engineId", engineId)
                        .queryParam("gearBoxId", gearBoxId)
                        .queryParam("transmissionId", transmissionId)
                        .queryParam("wheelId", wheelId)
                        .queryParam("interiorId", interiorId)
                        .queryParam("rudderId", rudderId)
                        .queryParam("color", colorHex)
                        .build())
                .retrieve()
                .body(ConfigurationDTO.class);
    }

    private RestClient restClient() {
        RestClient.Builder builder = restClientBuilder.baseUrl(storageBaseUrl);
        if (SecurityContextHolder.getContext().getAuthentication() instanceof JwtAuthenticationToken jwtAuth) {
            builder = builder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAuth.getToken().getTokenValue());
        }
        return builder.build();
    }
}
