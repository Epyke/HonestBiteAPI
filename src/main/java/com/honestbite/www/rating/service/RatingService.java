package com.honestbite.www.rating.service;

import com.honestbite.www.rating.dto.RatingDTO;
import com.honestbite.www.rating.model.RatingEntity;
import com.honestbite.www.rating.persistence.RatingRepository;
import com.honestbite.www.restaurant.model.RestaurantEntity;
import com.honestbite.www.restaurant.persistence.RestaurantRepository;
import com.honestbite.www.user.model.UserEntity;
import com.honestbite.www.user.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public RatingDTO.PostOutput saveRating(RatingDTO.PostInput input, String userEmail) {

        UserEntity user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("Utilizador não encontrado");
        }

        RestaurantEntity restaurant = restaurantRepository.findById(input.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurante não encontrado"));

        RatingEntity newRating = RatingEntity.builder()
                .score(input.getScore())
                .comment(input.getComment())
                .createdAt(LocalDateTime.now())
                .user(user)
                .restaurant(restaurant)
                .build();

        ratingRepository.save(newRating);
        return new RatingDTO.PostOutput("Review criada com sucesso");
    }
}