package com.foodordering.service;

import com.foodordering.dto.request.OrderStatusRequest;
import com.foodordering.dto.response.CartResponse;
import com.foodordering.dto.response.OrderResponse;
import com.foodordering.entity.*;
import com.foodordering.exception.BadRequestException;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.repository.OrderRepository;
import com.foodordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final EmailService emailService;
    private final AdminLogService adminLogService;

    @Transactional
    public OrderResponse placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CartResponse cartResponse = cartService.getCart(userId);
        if (cartResponse.getItems() == null || cartResponse.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.PENDING)
                .totalAmount(cartResponse.getTotal())
                .orderItems(new ArrayList<>())
                .build();

        for (var ci : cartResponse.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(ci.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + ci.getMenuItemId()));
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(ci.getQuantity())
                    .price(ci.getPrice())
                    .build();
            order.getOrderItems().add(orderItem);
        }

        order = orderRepository.save(order);
        cartService.clearCart(userId);

        log.info("Order placed: #{} by user {}", order.getId(), user.getEmail());
        emailService.sendOrderConfirmationEmail(user, order);

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new BadRequestException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        log.info("Order cancelled: #{} by user {}", orderId, userId);
        emailService.sendOrderCancellationEmail(order.getUser(), order);

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, OrderStatusRequest request, User admin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(request.getStatus());
        order = orderRepository.save(order);

        adminLogService.log(admin, "UPDATE_ORDER_STATUS",
                String.format("Order #%d status: %s -> %s", orderId, oldStatus, request.getStatus()));
        log.info("Order #{} status updated to {}", orderId, request.getStatus());

        if (request.getStatus() == OrderStatus.CANCELLED) {
            emailService.sendOrderCancellationEmail(order.getUser(), order);
        }

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse adminCancelOrder(Long orderId, User admin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        adminLogService.log(admin, "ADMIN_CANCEL_ORDER", "Cancelled order #" + orderId);
        emailService.sendOrderCancellationEmail(order.getUser(), order);

        return OrderResponse.from(order);
    }

    public List<OrderResponse> getUserOrderHistory(Long userId) {
        return getUserOrders(userId);
    }
}
