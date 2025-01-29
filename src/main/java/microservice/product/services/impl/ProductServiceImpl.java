package microservice.product.services.impl;

import jakarta.persistence.EntityNotFoundException;
import microservice.product.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.product.dtos.productDTO.*;
import microservice.product.exceptions.ApplicationException;
import microservice.product.exceptions.InsufficientStockException;
import microservice.product.mappers.ProductMapper;
import microservice.product.models.Product;
import microservice.product.repositories.ProductRepository;
import microservice.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = productMapper.toEntity(productRequestDTO);
        productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    @Override
    public ProductResponseDTO updateProduct(ProductUpdateDTO productUpdateDTO) {
        Product product = productRepository.findById(productUpdateDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("El ID del producto no fue encontrado"));
        if(productUpdateDTO.name() != null && !productUpdateDTO.name().isBlank()) {
            product.setName(productUpdateDTO.name());
        }
        if(productUpdateDTO.description() != null && !productUpdateDTO.description().isBlank()) {
            product.setDescription(productUpdateDTO.description());
        }
        if(productUpdateDTO.price() != null) {
            product.setPrice(productUpdateDTO.price());
        }
        if(productUpdateDTO.stock() != null) {
            product.setStock(productUpdateDTO.stock());
        }
        productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    @Override
    public Set<ProductResponseDTO> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productMapper.toResponseSetDTO(productList);
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el ID del producto ingresado"));
        productRepository.delete(product);
    }


    @Override
    public void reserveStock(Set<OrderItemRequestDTO> items) {
        List<String> unavailableProducts = new ArrayList<>();

        for (OrderItemRequestDTO item : items) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: ID " + item.productId()));

            if (product.getStock() < item.quantity()) {
                unavailableProducts.add("Producto: " + product.getName() + " (ID: " + item.productId() + ")");
            } else {
                product.setStock(product.getStock() - item.quantity());
                productRepository.save(product);
            }
        }

        if (!unavailableProducts.isEmpty()) {
            throw new InsufficientStockException("Stock insuficiente", unavailableProducts);
        }
    }

    @Override
    public Map<Long, String> getProductNames(Set<Long> productIds) {
        Map<Long, String> productNames = productRepository.findAllById(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, Product::getName));
        return productNames;
    }

    @Override
    public Map<Long, ProductDTO> getProductDetails(Set<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        return products.stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        product -> new ProductDTO(
                                product.getName(),
                                product.getDescription(),
                                product.getPrice(),
                                product.getStock()))
                );
    }

}
