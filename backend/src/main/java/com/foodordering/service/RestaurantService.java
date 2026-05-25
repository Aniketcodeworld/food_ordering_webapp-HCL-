package com.foodordering.service;

import com.foodordering.dto.request.RestaurantRequest;
import com.foodordering.dto.response.RestaurantResponse;
import com.foodordering.entity.Restaurant;
import com.foodordering.entity.User;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AdminLogService adminLogService;

    public List<RestaurantResponse> getAllActive() {
        return restaurantRepository.findByActiveTrue().stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    public List<RestaurantResponse> getAll() {
        return restaurantRepository.findAll().stream()
                .map(RestaurantResponse::from)
                .collect(Collectors.toList());
    }

    public RestaurantResponse getById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        return RestaurantResponse.from(restaurant);
    }

    @Transactional
    public RestaurantResponse create(RestaurantRequest request, User admin) {
        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .imageUrl(request.getImageUrl())
                .active(request.getActive() == null || request.getActive())
                .build();
        restaurant = restaurantRepository.save(restaurant);
        adminLogService.log(admin, "CREATE_RESTAURANT", "Created restaurant: " + restaurant.getName());
        return RestaurantResponse.from(restaurant);
    }

    @Transactional
    public RestaurantResponse update(Long id, RestaurantRequest request, User admin) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setAddress(request.getAddress());
        restaurant.setImageUrl(request.getImageUrl());
        if (request.getActive() != null) {
            restaurant.setActive(request.getActive());
        }

        restaurant = restaurantRepository.save(restaurant);
        adminLogService.log(admin, "UPDATE_RESTAURANT", "Updated restaurant ID: " + id);
        return RestaurantResponse.from(restaurant);
    }

    @Transactional
    public void delete(Long id, User admin) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));
        restaurantRepository.delete(restaurant);
        adminLogService.log(admin, "DELETE_RESTAURANT", "Deleted restaurant: " + restaurant.getName());
    }
}
