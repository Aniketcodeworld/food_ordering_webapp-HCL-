package com.foodordering.config;

import com.foodordering.entity.*;
import com.foodordering.repository.CartRepository;
import com.foodordering.repository.RestaurantRepository;
import com.foodordering.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final RestaurantRepository restaurantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = User.builder()
                    .name("Admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("0000000000")
                    .role(Role.ROLE_ADMIN)
                    .build();
            admin = userRepository.save(admin);
            cartRepository.save(Cart.builder().user(admin).build());
        }

        if (restaurantRepository.count() == 0) {
            seedRestaurants();
        }
    }

    private void seedRestaurants() {
        Restaurant r1 = Restaurant.builder()
                .name("Pizza Palace")
                .description("Best pizzas in town with fresh ingredients")
                .address("123 Main Street, New York")
                .imageUrl("https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400")
                .active(true)
                .build();

        Restaurant r2 = Restaurant.builder()
                .name("Burger Hub")
                .description("Juicy burgers and crispy fries")
                .address("456 Oak Avenue, Los Angeles")
                .imageUrl("https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400")
                .active(true)
                .build();

        Restaurant r3 = Restaurant.builder()
                .name("Sushi Express")
                .description("Fresh sushi and Japanese cuisine")
                .address("789 Pine Road, Chicago")
                .imageUrl("https://images.unsplash.com/photo-1579584425555-c3ce17fd1871?w=400")
                .active(true)
                .build();

        r1.getMenuItems().add(MenuItem.builder().name("Margherita Pizza").description("Classic tomato and mozzarella").price(new BigDecimal("12.99")).imageUrl("https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400").available(true).restaurant(r1).build());
        r1.getMenuItems().add(MenuItem.builder().name("Pepperoni Pizza").description("Spicy pepperoni with cheese").price(new BigDecimal("14.99")).imageUrl("https://images.unsplash.com/photo-1628840042765-356cda07504e?w=400").available(true).restaurant(r1).build());
        r1.getMenuItems().add(MenuItem.builder().name("Garlic Bread").description("Toasted bread with garlic butter").price(new BigDecimal("5.99")).imageUrl("https://images.unsplash.com/photo-1619535860434-ba1d8fa12536?w=400").available(true).restaurant(r1).build());

        r2.getMenuItems().add(MenuItem.builder().name("Classic Burger").description("Beef patty with lettuce and tomato").price(new BigDecimal("9.99")).imageUrl("https://images.unsplash.com/photo-1550547660-d9450f859349?w=400").available(true).restaurant(r2).build());
        r2.getMenuItems().add(MenuItem.builder().name("Cheese Burger").description("Double cheese beef burger").price(new BigDecimal("11.99")).imageUrl("https://images.unsplash.com/photo-1586190848861-99aa4a171e09?w=400").available(true).restaurant(r2).build());
        r2.getMenuItems().add(MenuItem.builder().name("French Fries").description("Crispy golden fries").price(new BigDecimal("4.99")).imageUrl("https://images.unsplash.com/photo-1573080496219-b080abfee13c?w=400").available(true).restaurant(r2).build());

        r3.getMenuItems().add(MenuItem.builder().name("California Roll").description("Crab, avocado, cucumber").price(new BigDecimal("8.99")).imageUrl("https://images.unsplash.com/photo-1553621042-f6e147245754?w=400").available(true).restaurant(r3).build());
        r3.getMenuItems().add(MenuItem.builder().name("Salmon Nigiri").description("Fresh salmon over rice").price(new BigDecimal("10.99")).imageUrl("https://images.unsplash.com/photo-1611145437889-6265d50a0f06?w=400").available(true).restaurant(r3).build());
        r3.getMenuItems().add(MenuItem.builder().name("Miso Soup").description("Traditional Japanese soup").price(new BigDecimal("3.99")).imageUrl("https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400").available(true).restaurant(r3).build());

        restaurantRepository.saveAll(List.of(r1, r2, r3));
    }
}
