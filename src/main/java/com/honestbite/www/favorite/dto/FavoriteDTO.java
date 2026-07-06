package com.honestbite.www.favorite.dto;

import com.honestbite.www.restaurant.dto.RestaurantDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

public class FavoriteDTO {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class StatusResponse {
        private boolean favorited;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserFavoriteListOutput {
        private Long id;
        private LocalDateTime favoritedAt;
        private List<RestaurantDTO.GetOutputAllRest> restaurant;
    }
}