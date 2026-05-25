package com.foodordering.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RestaurantRequest {
    @NotBlank(message = "Restaurant name is required")
    private String name;
    private String description;
    private String address;
    private String imageUrl;
    private Boolean active;
}
