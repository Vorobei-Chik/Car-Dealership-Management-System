package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.car.model.ConfigurationDTO;
import study.project.dealership.domain.car.Configuration;

public final class ConfigurationMapper {

    private ConfigurationMapper() {}

    @Contract("_ -> new")
    public static @NotNull ConfigurationDTO toDto(@NotNull Configuration config) {
        return new ConfigurationDTO(
                config.getId(),
                ModelMapper.toDto(config.getCarModel()),
                PartMapper.toDto(config.getEngine()),
                PartMapper.toDto(config.getGearBox()),
                PartMapper.toDto(config.getTransmission()),
                PartMapper.toDto(config.getWheels()),
                PartMapper.toDto(config.getInterior()),
                PartMapper.toDto(config.getRudder()),
                ValueObjectMapper.toString(config.getColor()),
                config.isStandard()
        );
    }
}
