package microservice.product.dtos.OrderItemDTO;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequestDTO(
        @NotNull(message = "El poductId no debe ser nulo")
        Long productId,
        @NotNull(message = "quantity no debe ser nulo")
        Integer quantity
) {
}
