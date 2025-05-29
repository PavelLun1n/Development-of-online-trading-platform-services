package com.example.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Long id;
    private Long sellerId;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private int stock;
    private Long categoryId;
    private String categoryCode;
    private LocalDateTime createdAt;
}
