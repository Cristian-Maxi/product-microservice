package microservice.product.controllers;

import jakarta.validation.Valid;
import microservice.product.dtos.ApiResponseDTO;
import microservice.product.dtos.OrderItemDTO.OrderItemRequestDTO;
import microservice.product.dtos.productDTO.*;
import microservice.product.exceptions.ApplicationException;
import microservice.product.exceptions.InsufficientStockException;
import microservice.product.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                            @RequestHeader("X-User-Authorities") String roles) {
        if (!roles.contains("ADMIN")) {
            throw new ApplicationException("Acceso denegado: Debes ser un ADMIN para crear un producto");
        }
        try {
            ProductResponseDTO productResponseDTO = productService.createProduct(productRequestDTO);
            return new ResponseEntity<>(productResponseDTO, HttpStatus.CREATED);
        } catch (ApplicationException e) {
            throw new ApplicationException("Ha ocurrido un error: " + e.getMessage());
        }
    }

    @GetMapping("/perfil")
    public ResponseEntity<?> getPerfil(@RequestHeader("X-User-Name") String username, @RequestHeader("X-User-Authorities") String authorities) {
        List<String> roles = Arrays.asList(authorities.split(","));
        return ResponseEntity.ok(Map.of("username", username, "roles", roles));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> updateProduct(@Valid @RequestBody ProductUpdateDTO productUpdateDTO,
                                                                            @RequestHeader("X-User-Authorities") String roles) {
        if (!roles.contains("ADMIN")) {
            throw new ApplicationException("Acceso denegado: Debes ser un ADMIN para crear un producto");
        }
        ProductResponseDTO productResponseDTO = productService.updateProduct(productUpdateDTO);
        String message = "Producto Actualizado";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, message, productResponseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEntity(@PathVariable Long id, @RequestHeader("X-User-Authorities") String roles) {
        if (!roles.contains("ADMIN")) {
            throw new ApplicationException("Acceso denegado: Debes ser un ADMIN para eliminar un producto");
        }
        productService.delete(id);
        return new ResponseEntity<>("Producto Eliminado exitosamente", HttpStatus.OK);
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<ApiResponseDTO<ProductResponseDTO>> getAllProducts() {
        try {
            Set<ProductResponseDTO> userEntityResponseDTO = productService.getAllProducts();
            if (userEntityResponseDTO.isEmpty()) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "No se ingresaron Productos", userEntityResponseDTO), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponseDTO<>(true, "Productos guardados", userEntityResponseDTO), HttpStatus.OK);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(" Ha ocurrido un error " + e.getMessage());
        }
    }

    @PostMapping("/reserve-stock")
    public ResponseEntity<ApiResponseDTO<String>> reserveStock(@RequestBody Set<OrderItemRequestDTO> items) {
        try {
            productService.reserveStock(items);
            return new ResponseEntity<>(new ApiResponseDTO<>(true, "Stock reservado exitosamente", null), HttpStatus.OK);
        } catch (InsufficientStockException e) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, e.getMessage(), e.getUnavailableProducts()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponseDTO<>(false, "Error al reservar stock: " + e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/names")
    public ResponseEntity<Map<Long, String>> getProductNames(@RequestBody Set<Long> productIds) {
        Map<Long, String> productNames = productService.getProductNames(productIds);
        return ResponseEntity.ok(productNames);
    }

    @PostMapping("/details")
    public ResponseEntity<Map<Long, ProductDTO>> getProductDetails(@RequestBody Set<Long> productIds) {
        Map<Long, ProductDTO> productDetails = productService.getProductDetails(productIds);
        return ResponseEntity.ok(productDetails);
    }

}