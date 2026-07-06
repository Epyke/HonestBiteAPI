package com.honestbite.www.favorite.service;

import com.honestbite.www.favorite.dto.FavoriteDTO;
import com.honestbite.www.favorite.model.FavoriteEntity;
import com.honestbite.www.favorite.persistence.FavoriteRepository;
import com.honestbite.www.restaurant.dto.RestaurantDTO;
import com.honestbite.www.restaurant.model.RestaurantEntity;
import com.honestbite.www.restaurant.persistence.RestaurantRepository;
import com.honestbite.www.user.model.UserEntity;
import com.honestbite.www.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    public FavoriteDTO.StatusResponse toggleFavorite(Long userId, Long restaurantId) {
        Optional<FavoriteEntity> favoriteOpt = favoriteRepository.findByUserIdAndRestaurantId(userId, restaurantId);

        if (favoriteOpt.isPresent()) {
            favoriteRepository.delete(favoriteOpt.get());
            return new FavoriteDTO.StatusResponse(false);
        } else {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilizador não encontrado"));
            RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                    .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

            FavoriteEntity newFavorite = FavoriteEntity.builder()
                    .user(user)
                    .restaurant(restaurant)
                    .build();

            favoriteRepository.save(newFavorite);
            return new FavoriteDTO.StatusResponse(true);
        }
    }

    public FavoriteDTO.StatusResponse getFavoriteStatus(Long userId, Long restaurantId) {
        return new FavoriteDTO.StatusResponse(favoriteRepository.existsByUserIdAndRestaurantId(userId, restaurantId));
    }

    private RestaurantDTO.GetOutputAllRest convertToRestaurantGetOutputFetchByID(RestaurantEntity entity) {
        Double globalScore = entity.getRatings().stream()
                .mapToDouble(rating -> rating.getScore())
                .average()
                .orElse(0.0);

        return RestaurantDTO.GetOutputAllRest.builder()
                .id(entity.getId())
                .name(entity.getName())
                .avgPrice(entity.getAvgPrice())
                .global(Math.round(globalScore * 10.0) / 10.0)
                .adress(entity.getAddress())
                .categories(entity.getCategories().stream()
                        .map(cat -> com.honestbite.www.category.dto.CategoryDTO.GetOutput.builder()
                                .id(cat.getId())
                                .label(cat.getLabel()) // Adjust these field mappings to match your exact CategoryDTO.GetOutput builder fields
                                .value(cat.getValue())
                                .color(cat.getColor())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public List<FavoriteDTO.UserFavoriteListOutput> getFavoritesByUser(Long userId) {
        return favoriteRepository.findByUserId(userId).stream()
                .map(entity -> {
                    RestaurantDTO.GetOutputAllRest restaurantDto = convertToRestaurantGetOutputFetchByID(entity.getRestaurant());

                    return FavoriteDTO.UserFavoriteListOutput.builder()
                            .id(entity.getId())
                            .favoritedAt(entity.getFavoritedAt())
                            .restaurant(List.of(restaurantDto))
                            .build();
                })
                .collect(Collectors.toList());
    }
}