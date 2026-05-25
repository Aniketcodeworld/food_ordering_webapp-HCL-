package com.foodordering.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardResponse {
    private long totalUsers;
    private long totalRestaurants;
    private long totalOrders;
    private BigDecimal totalRevenue;
    private List<OrderResponse> recentOrders;
}
