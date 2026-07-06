package com.honestbite.www.favorite.controller;

import com.honestbite.www.favorite.dto.FavoriteDTO;
import com.honestbite.www.favorite.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/user/{userId}/toggle/{restaurantId}")
    public ResponseEntity<FavoriteDTO.StatusResponse> toggleFavorite(
            @PathVariable Long userId,
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(favoriteService.toggleFavorite(userId, restaurantId));
    }

    @GetMapping("/user/{userId}/status/{restaurantId}")
    public ResponseEntity<FavoriteDTO.StatusResponse> getFavoriteStatus(
            @PathVariable Long userId,
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(favoriteService.getFavoriteStatus(userId, restaurantId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteDTO.UserFavoriteListOutput>> getUserFavorites(
            @PathVariable Long userId) {
        List<FavoriteDTO.UserFavoriteListOutput> userFavorites = favoriteService.getFavoritesByUser(userId);
        return ResponseEntity.ok(userFavorites);
    }
}