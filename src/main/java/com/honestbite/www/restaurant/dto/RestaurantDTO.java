package com.honestbite.www.restaurant.dto;

import com.honestbite.www.adress.dto.AdressDTO;
import com.honestbite.www.adress.model.AdressEntity;
import com.honestbite.www.category.dto.CategoryDTO;
import com.honestbite.www.category.model.CategoryEntity;
import com.honestbite.www.ophour.dto.OpHourDTO;
import com.honestbite.www.rating.dto.RatingDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        String street;
        String city;
        Double global;

        List<OpHourDTO> schedule;
        List<RatingDTO.DisplayReview> reviews;
        List<String> menuPhotos;
        List<CategoryDTO.GetOutput> categories;
    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class GetOutputAllRest{
        Long id;
        String cover;
        String name;
        Double avgPrice;
        Double global;
        AdressEntity adress;
        List<CategoryDTO.GetOutput> categories;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateInput{
        String name;
        String description;
        String phoneNumber;
        Double avgPrice;
        String mapsUrl;
        AdressDTO.AddressInput address;
        List<Long> categoryIds;
        List<OpHourInput> opHours;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OpHourInput{
        String day;               // matches DayEntity.ptName, e.g. "Segunda"
        Boolean isClosed;
        List<IntervalInput> intervals;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class IntervalInput{
        String open;              // "HH:mm"
        String close;             // "HH:mm"
    }
}