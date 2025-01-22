package microservice.product.dtos.productDTO;

public record ProductResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock
) {
}
