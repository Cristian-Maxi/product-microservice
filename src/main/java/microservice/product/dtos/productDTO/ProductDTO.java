package microservice.product.dtos.productDTO;

public record ProductDTO(
        String name,
        String description,
        Double price,
        Integer stock
) {
}
