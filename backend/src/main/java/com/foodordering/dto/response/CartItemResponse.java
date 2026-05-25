package com.foodordering.dto.response;

import com.foodordering.entity.CartItem;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CartItemResponse {
    private Long id;
    private Long menuItemId;
    private String itemName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal subtotal;
    private String imageUrl;
    private Long restaurantId;

    public static CartItemResponse from(CartItem ci) {
        BigDecimal subtotal = ci.getMenuItem().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
        return CartItemResponse.builder()
                .id(ci.getId())
                .menuItemId(ci.getMenuItem().getId())
                .itemName(ci.getMenuItem().getName())
                .price(ci.getMenuItem().getPrice())
                .quantity(ci.getQuantity())
                .subtotal(subtotal)
                .imageUrl(ci.getMenuItem().getImageUrl())
                .restaurantId(ci.getMenuItem().getRestaurant().getId())
                .build();
    }
}
