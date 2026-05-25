package com.foodordering.dto.response;

import com.foodordering.entity.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String imageUrl;
    private boolean active;
    private int menuItemCount;

    public static RestaurantResponse from(Restaurant r) {
        return RestaurantResponse.builder()
                .id(r.getId())
                .name(r.getName())
                .description(r.getDescription())
                .address(r.getAddress())
                .imageUrl(r.getImageUrl())
                .active(r.isActive())
                .menuItemCount(r.getMenuItems() != null ? r.getMenuItems().size() : 0)
                .build();
    }
}
