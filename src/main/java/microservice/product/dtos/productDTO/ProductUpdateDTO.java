package microservice.product.dtos.productDTO;

import jakarta.validation.constraints.NotNull;

public record ProductUpdateDTO(
        @NotNull(message = "El id no debe ser nulo")
        Long id,
        String name,
        String description,
        Double price,
        Integer stock
) {
}
