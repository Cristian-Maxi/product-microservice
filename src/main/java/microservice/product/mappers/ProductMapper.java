package microservice.product.mappers;

import microservice.product.dtos.productDTO.ProductRequestDTO;
import microservice.product.dtos.productDTO.ProductResponseDTO;
import microservice.product.models.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public Product toEntity(ProductRequestDTO productRequestDTO) {
        return new Product(
                productRequestDTO.name(),
                productRequestDTO.description(),
                productRequestDTO.price(),
                productRequestDTO.stock()
        );
    }

    public ProductResponseDTO toResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock()
        );
    }

    public Set<ProductResponseDTO> toResponseSetDTO(List<Product> products) {
        return products.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());
    }
}
