package com.product.service;

import com.product.model.OrderItem;
import com.product.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Async("taskExecutor")
    public CompletableFuture<List<OrderItem>> getAllOrderItemsAsync() {
        return CompletableFuture.supplyAsync(() -> orderItemRepository.findOrderItems());
    }

    @Async("taskExecutor")
    public CompletableFuture<OrderItem> getOrderItemByIdAsync(String orderItemId) {
        return CompletableFuture.supplyAsync(() -> orderItemRepository.findById(Long.valueOf(orderItemId))
                .orElseThrow(() -> new RuntimeException("OrderItem not found")));
    }

    @Async("taskExecutor")
    public CompletableFuture<OrderItem> createOrderItemAsync(OrderItem orderItem) {
        return CompletableFuture.supplyAsync(() -> createOrderItem(orderItem));
    }

    @Transactional
    public OrderItem createOrderItem(OrderItem orderItem) {
        // The transaction is active here, so Hibernate can perform lazy loading or saving
        return orderItemRepository.save(orderItem);
    }

    @Async("taskExecutor")
    public CompletableFuture<OrderItem> updateOrderItemAsync(String orderItemId, OrderItem updatedOrderItem) {
        return CompletableFuture.supplyAsync(() -> {
            OrderItem existingOrderItem = orderItemRepository.findById(Long.valueOf(orderItemId))
                    .orElseThrow(() -> new RuntimeException("OrderItem not found"));
            existingOrderItem.setQuantity(updatedOrderItem.getQuantity());
            return orderItemRepository.save(existingOrderItem);
        });
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> deleteOrderItemAsync(String orderItemId) {
        return CompletableFuture.runAsync(() -> orderItemRepository.deleteById(Long.valueOf(orderItemId)));
    }
}
