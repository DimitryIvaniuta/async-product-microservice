package com.product.repository;

import com.product.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT p FROM Product p")
    List<Product> findProducts();

}
