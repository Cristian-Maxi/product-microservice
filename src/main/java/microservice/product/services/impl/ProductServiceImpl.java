package microservice.product.services.impl;

import jakarta.persistence.EntityNotFoundException;
import microservice.product.dtos.productDTO.*;
import microservice.product.mappers.ProductMapper;
import microservice.product.models.Product;
import microservice.product.repositories.ProductRepository;
import microservice.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

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
}
