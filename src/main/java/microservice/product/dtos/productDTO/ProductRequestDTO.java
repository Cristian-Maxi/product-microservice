package microservice.product.dtos.productDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(
        @NotBlank(message = "name no debe estar vacio")
        String name,
        @NotBlank(message = "description no debe estar vacio")
        String description,
        @NotNull(message = "price no debe ser nulo")
        Double price,
        @NotNull(message = "stock no debe ser nulo")
        Integer stock
) {
}
