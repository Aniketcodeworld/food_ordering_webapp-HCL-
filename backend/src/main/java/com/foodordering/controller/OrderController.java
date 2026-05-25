package com.foodordering.controller;

import com.foodordering.dto.response.ApiResponse;
import com.foodordering.dto.response.OrderResponse;
import com.foodordering.security.SecurityUtils;
import com.foodordering.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Orders", description = "User order operations")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @Operation(summary = "Place order from cart")
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder() {
        OrderResponse order = orderService.placeOrder(SecurityUtils.getCurrentUserId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order placed successfully", order));
    }

    @GetMapping
    @Operation(summary = "Get user order history")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders() {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getUserOrders(SecurityUtils.getCurrentUserId())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(id)));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.cancelOrder(SecurityUtils.getCurrentUserId(), id)));
    }
}
