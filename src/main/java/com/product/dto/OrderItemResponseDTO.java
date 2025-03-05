package com.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {

    private Long id;

    private LocalDateTime purchaseDate;

    private int quantity;

    private UserResponseDTO user;

    private ProductResponseDTO product;

}
