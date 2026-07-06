package com.honestbite.www.rating.dto;

import lombok.*;

public class RatingDTO {
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DisplayReview{
        private Long id;
        private String username;
        private Double score;
        private String createdAt;
        private String comment;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostInput{
        private Long restaurantId;
        private Double score;
        private String comment;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class PostOutput{
        private String response;
    }
}
