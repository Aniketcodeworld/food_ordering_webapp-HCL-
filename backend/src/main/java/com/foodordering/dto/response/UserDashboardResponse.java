package com.foodordering.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDashboardResponse {
    private UserResponse profile;
    private long totalOrders;
    private List<OrderResponse> recentOrders;
    private CartResponse cartSummary;
}
