package com.foodordering.controller;

import com.foodordering.dto.request.MenuItemRequest;
import com.foodordering.dto.request.OrderStatusRequest;
import com.foodordering.dto.request.RestaurantRequest;
import com.foodordering.dto.response.*;
import com.foodordering.security.CustomUserDetails;
import com.foodordering.security.SecurityUtils;
import com.foodordering.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {

    private final RestaurantService restaurantService;
    private final MenuService menuService;
    private final OrderService orderService;
    private final UserService userService;
    private final AdminLogService adminLogService;

    private CustomUserDetails currentAdmin() {
        return SecurityUtils.getCurrentUser();
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Admin dashboard analytics")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAdminDashboard()));
    }

    @GetMapping("/logs")
    @Operation(summary = "Get admin activity logs")
    public ResponseEntity<ApiResponse<List<AdminLogResponse>>> getLogs() {
        return ResponseEntity.ok(ApiResponse.success(adminLogService.getRecentLogs()));
    }

    // Restaurants
    @GetMapping("/restaurants")
    public ResponseEntity<ApiResponse<List<RestaurantResponse>>> getAllRestaurants() {
        return ResponseEntity.ok(ApiResponse.success(restaurantService.getAll()));
    }

    @PostMapping("/restaurants")
    public ResponseEntity<ApiResponse<RestaurantResponse>> createRestaurant(@Valid @RequestBody RestaurantRequest request) {
        RestaurantResponse response = restaurantService.create(request, currentAdmin().getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Restaurant created", response));
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse<RestaurantResponse>> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
        return ResponseEntity.ok(ApiResponse.success(restaurantService.update(id, request, currentAdmin().getUser())));
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRestaurant(@PathVariable Long id) {
        restaurantService.delete(id, currentAdmin().getUser());
        return ResponseEntity.ok(ApiResponse.success("Restaurant deleted", null));
    }

    // Menu
    @GetMapping("/menu")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getAllMenu() {
        return ResponseEntity.ok(ApiResponse.success(menuService.getAll()));
    }

    @PostMapping("/menu")
    public ResponseEntity<ApiResponse<MenuItemResponse>> createMenuItem(@Valid @RequestBody MenuItemRequest request) {
        MenuItemResponse response = menuService.create(request, currentAdmin().getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Menu item created", response));
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<ApiResponse<MenuItemResponse>> updateMenuItem(@PathVariable Long id, @Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(menuService.update(id, request, currentAdmin().getUser())));
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMenuItem(@PathVariable Long id) {
        menuService.delete(id, currentAdmin().getUser());
        return ResponseEntity.ok(ApiResponse.success("Menu item deleted", null));
    }

    // Orders
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders()));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusRequest request) {
        return ResponseEntity.ok(ApiResponse.success(orderService.updateOrderStatus(id, request, currentAdmin().getUser())));
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.adminCancelOrder(id, currentAdmin().getUser())));
    }

    // Users
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id, currentAdmin().getUser());
        return ResponseEntity.ok(ApiResponse.success("User deleted", null));
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getUserOrderHistory(id)));
    }
}
