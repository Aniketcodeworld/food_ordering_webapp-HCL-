package com.foodordering.service;

import com.foodordering.dto.request.MenuItemRequest;
import com.foodordering.dto.response.MenuItemResponse;
import com.foodordering.entity.MenuItem;
import com.foodordering.entity.Restaurant;
import com.foodordering.entity.User;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final AdminLogService adminLogService;

    public List<MenuItemResponse> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantIdAndAvailableTrue(restaurantId).stream()
                .map(MenuItemResponse::from)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponse> getAllByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(MenuItemResponse::from)
                .collect(Collectors.toList());
    }

    public List<MenuItemResponse> getAll() {
        return menuItemRepository.findAll().stream()
                .map(MenuItemResponse::from)
                .collect(Collectors.toList());
    }

    public MenuItemResponse getById(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        return MenuItemResponse.from(item);
    }

    @Transactional
    public MenuItemResponse create(MenuItemRequest request, User admin) {
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        MenuItem item = MenuItem.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .available(request.getAvailable() == null || request.getAvailable())
                .restaurant(restaurant)
                .build();

        item = menuItemRepository.save(item);
        adminLogService.log(admin, "CREATE_MENU_ITEM", "Created menu item: " + item.getName());
        return MenuItemResponse.from(item);
    }

    @Transactional
    public MenuItemResponse update(Long id, MenuItemRequest request, User admin) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setImageUrl(request.getImageUrl());
        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }
        item.setRestaurant(restaurant);

        item = menuItemRepository.save(item);
        adminLogService.log(admin, "UPDATE_MENU_ITEM", "Updated menu item ID: " + id);
        return MenuItemResponse.from(item);
    }

    @Transactional
    public void delete(Long id, User admin) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
        menuItemRepository.delete(item);
        adminLogService.log(admin, "DELETE_MENU_ITEM", "Deleted menu item: " + item.getName());
    }
}
