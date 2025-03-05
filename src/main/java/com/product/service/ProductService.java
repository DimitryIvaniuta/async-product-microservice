package com.product.service;

import com.product.model.Product;
import com.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Async("taskExecutor")
    public CompletableFuture<Product> fetchProductDetailsAsync(final Long productId) {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(500);
            return productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
        });
    }

    private void simulateDelay(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<List<Product>> getAllProductsAsync() {
        return CompletableFuture.supplyAsync(() -> productRepository.findProducts());
    }

    @Async("taskExecutor")
    public CompletableFuture<Product> getProductByIdAsync(String productId) {
        return CompletableFuture.supplyAsync(() -> productRepository.findById(Long.valueOf(productId))
                .orElseThrow(() -> new RuntimeException("Product not found")));
    }

    @Async("taskExecutor")
    public CompletableFuture<Product> createProductAsync(Product product) {
        return CompletableFuture.supplyAsync(() -> productRepository.save(product));
    }

    @Async("taskExecutor")
    public CompletableFuture<Product> updateProductAsync(String productId, Product updatedProduct) {
        return CompletableFuture.supplyAsync(() -> {
            Product existingProduct = productRepository.findById(Long.valueOf(productId))
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            return productRepository.save(existingProduct);
        });
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> deleteProductAsync(String productId) {
        return CompletableFuture.runAsync(() -> productRepository.deleteById(Long.valueOf(productId)));
    }

}
