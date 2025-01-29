package microservice.product.services;

import microservice.product.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.product.dtos.productDTO.ProductDTO;
import microservice.product.dtos.productDTO.ProductRequestDTO;
import microservice.product.dtos.productDTO.ProductResponseDTO;
import microservice.product.dtos.productDTO.ProductUpdateDTO;

import java.util.Map;
import java.util.Set;

public interface ProductService {
    ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO updateProduct(ProductUpdateDTO productUpdateDTO);
    void delete(Long id);
    Set<ProductResponseDTO> getAllProducts();
    void reserveStock(Set<OrderItemRequestDTO> items);
    Map<Long, String> getProductNames(Set<Long> productIds);
    Map<Long, ProductDTO> getProductDetails(Set<Long> productIds);
}