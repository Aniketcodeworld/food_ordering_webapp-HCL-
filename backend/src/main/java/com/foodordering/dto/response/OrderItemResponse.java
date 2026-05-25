package com.foodordering.dto.response;

import com.foodordering.entity.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemResponse {
    private Long id;
    private Long menuItemId;
    private String itemName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public static OrderItemResponse from(OrderItem oi) {
        return OrderItemResponse.builder()
                .id(oi.getId())
                .menuItemId(oi.getMenuItem().getId())
                .itemName(oi.getMenuItem().getName())
                .quantity(oi.getQuantity())
                .price(oi.getPrice())
                .subtotal(oi.getPrice().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .build();
    }
}
