package com.foodordering.repository;

import com.foodordering.entity.Order;
import com.foodordering.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems", "orderItems.menuItem", "user"})
    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.menuItem", "user"})
    List<Order> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"orderItems", "orderItems.menuItem", "user"})
    Optional<Order> findWithDetailsById(Long id);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.status <> 'CANCELLED'")
    BigDecimal calculateTotalRevenue();

    long countByStatusNot(OrderStatus status);

    List<Order> findTop5ByOrderByCreatedAtDesc();
}
