package com.product.repository;

import com.product.model.OrderItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {

    @Query("SELECT p FROM OrderItem p")
    List<OrderItem> findOrderItems();

}
