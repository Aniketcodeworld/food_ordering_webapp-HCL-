package com.foodordering.dto.request;

import com.foodordering.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusRequest {
    @NotNull(message = "Order status is required")
    private OrderStatus status;
}
