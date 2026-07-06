package com.honestbite.www.restaurant.dto;

import com.honestbite.www.adress.model.AdressEntity;
import com.honestbite.www.category.dto.CategoryDTO;
import com.honestbite.www.category.model.CategoryEntity;
import com.honestbite.www.ophour.dto.OpHourDTO;
import com.honestbite.www.rating.dto.RatingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class RestaurantDTO {
    @Data
    @AllArgsConstructor
    @Builder
    public static class GetInputFetchByID{
        Long id;
    }
    @Data
    @AllArgsConstructor
    @Builder
    public static class GetOutputFetchByID{
        Long id;
        String name;
        String description;
        String phoneNumber;
        String mapsUrl;
        Double avgPrice;
        String logo;
        String cover;
        String city;
        Double global;

        List<OpHourDTO> schedule;
        List<RatingDTO.DisplayReview> reviews;
        List<String> menuPhotos;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class GetOutputAllRest{
        Long id;
        String name;
        Double avgPrice;
        Double global;
        AdressEntity adress;
        List<CategoryDTO.GetOutput> categories;
    }
}