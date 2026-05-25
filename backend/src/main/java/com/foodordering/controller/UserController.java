package com.foodordering.controller;

import com.foodordering.dto.response.ApiResponse;
import com.foodordering.dto.response.UserDashboardResponse;
import com.foodordering.dto.response.UserResponse;
import com.foodordering.security.SecurityUtils;
import com.foodordering.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "User profile and dashboard")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "Get user profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        return ResponseEntity.ok(ApiResponse.success(
                userService.getProfile(SecurityUtils.getCurrentUserId())));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get user dashboard")
    public ResponseEntity<ApiResponse<UserDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(
                userService.getUserDashboard(SecurityUtils.getCurrentUserId())));
    }
}
