package com.foodordering.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MenuItemRequest {
    @NotBlank(message = "Item name is required")
    private String name;
    private String description;

    @NotNull @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    private String imageUrl;
    private Boolean available;

    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
}
