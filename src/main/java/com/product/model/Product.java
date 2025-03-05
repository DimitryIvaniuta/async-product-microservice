package com.product.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AP_UNIQUE_ID")
    @SequenceGenerator(name = "AP_UNIQUE_ID", sequenceName = "AP_UNIQUE_ID", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public Product() {
        //
    }

    public Product(final String name, final String description, final double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }


    // Convenience method to manage bidirectional relationship
    public void addPurchase(final OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setProduct(this);
    }

    public void removePurchase(final OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setProduct(null);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
