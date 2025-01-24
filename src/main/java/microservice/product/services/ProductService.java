package microservice.product.services;

import microservice.product.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.product.dtos.productDTO.ProductRequestDTO;
import microservice.product.dtos.productDTO.ProductResponseDTO;
import microservice.product.dtos.productDTO.ProductUpdateDTO;

import java.util.Set;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(ProductUpdateDTO productUpdateDTO);
    Set<ProductResponseDTO> getAllProducts();
    void reserveStock(Set<OrderItemRequestDTO> items);
}