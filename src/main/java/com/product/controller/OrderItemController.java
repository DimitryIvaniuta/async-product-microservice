package com.product.controller;

import com.product.dto.OrderItemRequestDTO;
import com.product.dto.OrderItemResponseDTO;
import com.product.dto.ProductResponseDTO;
import com.product.dto.UserResponseDTO;
import com.product.exception.NotFoundException;
import com.product.exception.OrderCreateException;
import com.product.model.OrderItem;
import com.product.model.Product;
import com.product.model.User;
import com.product.service.OrderItemService;
import com.product.service.ProductService;
import com.product.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order-items")
@Slf4j
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * Endpoint to create an OrderItem for a given user and product.
     * For simplicity, quantity is passed as a parameter.
     */
    @PostMapping("/create-order")
    public CompletableFuture<OrderItem> createOrderItem(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        // Validate input parameters
        if (userId == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("User ID must not be null or blank"));
        }
        if (productId == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Product ID must not be null or blank"));
        }
        if (quantity <= 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Quantity must be greater than 0"));
        }

        // Asynchronously fetch user and product details concurrently
        CompletableFuture<User> userFuture = userService.fetchUserDetailsAsync(userId);
        CompletableFuture<Product> productFuture = productService.fetchProductDetailsAsync(productId);

        return userFuture.thenCombine(productFuture, (user, product) -> {
                    // Optionally, further validate that user and product exist
                    if (Objects.isNull(user)) {
                        throw new NotFoundException("User with ID " + userId + " not found");
                    }
                    if (Objects.isNull(product)) {
                        throw new NotFoundException("Product with ID " + productId + " not found");
                    }
                    // Create new OrderItem
                    OrderItem orderItem = new OrderItem(quantity);
                    orderItem.setUser(user);
                    orderItem.setProduct(product);
                    return orderItem;
                })
                // Save the OrderItem asynchronously
                .thenCompose(orderItem ->
                        CompletableFuture.supplyAsync(() -> orderItemService.saveOrderItem(orderItem))
                )
                .exceptionally(ex -> {
                    // Log the exception (or rethrow as a custom exception)
                    log.error("Error creating order item: {}", ex.getMessage());
                    throw new OrderCreateException("Error creating order item", ex);
                });
    }

    // Get all order items
    @GetMapping
    public CompletableFuture<List<OrderItemResponseDTO>> getAllOrderItems() {
        return orderItemService.getAllOrderItemsAsync().thenApply(orderItems ->
                orderItems.stream().map(this::toOrderItemResponseDTO).collect(Collectors.toList())
        );
    }

    // Get order item by ID
    @GetMapping("/{id}")
    public CompletableFuture<OrderItemResponseDTO> getOrderItemById(@PathVariable("id") String orderItemId) {
        return orderItemService.getOrderItemByIdAsync(orderItemId).thenApply(this::toOrderItemResponseDTO);
    }

    // Create order item asynchronously with validation
    @PostMapping("/create")
    public CompletableFuture<OrderItemResponseDTO> createOrderItem(@RequestBody OrderItemRequestDTO requestDTO) {
        // Validate inputs
        if (requestDTO.getUserId() == null || requestDTO.getUserId().isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("User ID must not be null or blank"));
        }
        if (requestDTO.getProductId() == null || requestDTO.getProductId().isBlank()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Product ID must not be null or blank"));
        }
        if (requestDTO.getQuantity() <= 0) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Quantity must be greater than 0"));
        }

        CompletableFuture<User> userFuture = userService.getUserByIdAsync(requestDTO.getUserId());
        CompletableFuture<Product> productFuture = productService.getProductByIdAsync(requestDTO.getProductId());

        return userFuture.thenCombine(productFuture, (user, product) -> {
            OrderItem orderItem = new OrderItem(requestDTO.getQuantity());
            orderItem.setUser(user);
            orderItem.setProduct(product);
            return orderItem;
        }).thenCompose(orderItem ->
                orderItemService.createOrderItemAsync(orderItem)
        ).thenApply(this::toOrderItemResponseDTO);
    }

    // Update order item
    @PutMapping("/{id}")
    public CompletableFuture<OrderItemResponseDTO> updateOrderItem(@PathVariable("id") String orderItemId,
                                                                   @RequestBody OrderItemRequestDTO requestDTO) {
        // For update, we only update quantity in this example.
        OrderItem updateData = new OrderItem(requestDTO.getQuantity());
        return orderItemService.updateOrderItemAsync(orderItemId, updateData)
                .thenApply(this::toOrderItemResponseDTO);
    }

    // Delete order item
    @DeleteMapping("/{id}")
    public CompletableFuture<Void> deleteOrderItem(@PathVariable("id") String orderItemId) {
        return orderItemService.deleteOrderItemAsync(orderItemId);
    }

    // Mapper for OrderItem -> OrderItemResponseDTO
    private OrderItemResponseDTO toOrderItemResponseDTO(OrderItem orderItem) {
        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(orderItem.getId());
        dto.setPurchaseDate(orderItem.getPurchaseDate());
        dto.setQuantity(orderItem.getQuantity());

        // Map user to UserResponseDTO
        User user = orderItem.getUser();
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        dto.setUser(userDTO);

        // Map product to ProductResponseDTO
        Product product = orderItem.getProduct();
        ProductResponseDTO productDTO = new ProductResponseDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        dto.setProduct(productDTO);

        return dto;
    }

}
