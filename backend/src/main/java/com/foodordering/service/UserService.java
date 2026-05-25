package com.foodordering.service;

import com.foodordering.dto.response.DashboardResponse;
import com.foodordering.dto.response.OrderResponse;
import com.foodordering.dto.response.UserDashboardResponse;
import com.foodordering.dto.response.UserResponse;
import com.foodordering.entity.Role;
import com.foodordering.entity.User;
import com.foodordering.exception.BadRequestException;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.OrderRepository;
import com.foodordering.repository.RestaurantRepository;
import com.foodordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final CartService cartService;
    private final AdminLogService adminLogService;

    public UserResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.from(user);
    }

    public UserDashboardResponse getUserDashboard(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<OrderResponse> recentOrders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .limit(5)
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        long totalOrders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId).size();

        return UserDashboardResponse.builder()
                .profile(UserResponse.from(user))
                .totalOrders(totalOrders)
                .recentOrders(recentOrders)
                .cartSummary(cartService.getCart(userId))
                .build();
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findByRole(Role.ROLE_USER).stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(Long userId, User admin) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() == Role.ROLE_ADMIN) {
            throw new BadRequestException("Cannot delete admin user");
        }

        userRepository.delete(user);
        adminLogService.log(admin, "DELETE_USER", "Deleted user: " + user.getEmail());
    }

    public DashboardResponse getAdminDashboard() {
        List<OrderResponse> recentOrders = orderRepository.findTop5ByOrderByCreatedAtDesc().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        return DashboardResponse.builder()
                .totalUsers(userRepository.countByRole(Role.ROLE_USER))
                .totalRestaurants(restaurantRepository.count())
                .totalOrders(orderRepository.count())
                .totalRevenue(orderRepository.calculateTotalRevenue())
                .recentOrders(recentOrders)
                .build();
    }
}
