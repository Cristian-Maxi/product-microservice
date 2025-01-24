package microservice.product.dtos.OrderItemDTO;

public record OrderItemResponseDTO(
        Long id,
        Long productId,
        Integer quantity
) {
}
