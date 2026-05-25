package com.foodordering.controller;

import com.foodordering.dto.response.ApiResponse;
import com.foodordering.dto.response.MenuItemResponse;
import com.foodordering.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Browse menu items")
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get menu items by restaurant")
    public ResponseEntity<ApiResponse<List<MenuItemResponse>>> getByRestaurant(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(ApiResponse.success(menuService.getByRestaurant(restaurantId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get menu item by ID")
    public ResponseEntity<ApiResponse<MenuItemResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(menuService.getById(id)));
    }
}
