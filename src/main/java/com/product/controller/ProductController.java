package com.product.controller;

import com.product.dto.ProductRequestDTO;
import com.product.dto.ProductResponseDTO;
import com.product.model.Product;
import com.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/get-product/{id}")
    public CompletableFuture<Product> getProduct(@PathVariable("id") Long productId) {
        return productService.fetchProductDetailsAsync(productId)
                .exceptionally(ex -> {
                    log.error("Error fetching product: {}", ex.getMessage());
                    // Fallback: return a default product
                    Product fallback = new Product();
                    fallback.setId(productId);
                    fallback.setName("Fallback Product");
                    fallback.setDescription("Default description");
                    fallback.setPrice(249.99);
                    return fallback;
                });
    }

    // Get all products
    @GetMapping
    public CompletableFuture<List<ProductResponseDTO>> getAllProducts() {
        return productService.getAllProductsAsync().thenApply(products ->
                products.stream().map(this::toProductResponseDTO).collect(Collectors.toList())
        );
    }

    // Get product by ID
    @GetMapping("/{id}")
    public CompletableFuture<ProductResponseDTO> getProductById(@PathVariable("id") String productId) {
        return productService.getProductByIdAsync(productId).thenApply(this::toProductResponseDTO);
    }

    // Create new product
    @PostMapping
    public CompletableFuture<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO requestDTO) {
        Product product = new Product(requestDTO.getName(), requestDTO.getDescription(), requestDTO.getPrice());
        return productService.createProductAsync(product).thenApply(this::toProductResponseDTO);
    }

    // Update product
    @PutMapping("/{id}")
    public CompletableFuture<ProductResponseDTO> updateProduct(@PathVariable("id") String productId, @RequestBody ProductRequestDTO requestDTO) {
        Product updatedProduct = new Product(requestDTO.getName(), requestDTO.getDescription(), requestDTO.getPrice());
        return productService.updateProductAsync(productId, updatedProduct).thenApply(this::toProductResponseDTO);
    }

    // Delete product
    @DeleteMapping("/{id}")
    public CompletableFuture<Void> deleteProduct(@PathVariable("id") String productId) {
        return productService.deleteProductAsync(productId);
    }

    // Mapper method for Product to ProductResponseDTO
    private ProductResponseDTO toProductResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        return dto;
    }

}
