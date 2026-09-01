package study.project.dealership.contracts.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import study.project.dealership.contracts.car.model.ModelDTO;
import study.project.dealership.domain.car.Model;

public final class ModelMapper {

    private ModelMapper() {}

    @Contract("_ -> new")
    public static @NotNull ModelDTO toDto(@NotNull Model model) {
        return new ModelDTO(
                model.getId(),
                model.getBrand(),
                EnumMapper.toDto(model.getBodyType()),
                ValueObjectMapper.toBigDecimal(model.getPrice())
        );
    }
}
