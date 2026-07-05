package com.honestbite.www.restaurant.service;

import com.honestbite.www.days.model.DayEntity;
import com.honestbite.www.ophour.dto.OpHourDTO;
import com.honestbite.www.rating.dto.RatingDTO;
import com.honestbite.www.restaurant.dto.RestaurantDTO;
import com.honestbite.www.restaurant.model.RestaurantEntity;
import com.honestbite.www.restaurant.persistence.RestaurantRepository;
import com.honestbite.www.ophour.model.OpHourEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<RestaurantDTO.GetOutputAllRest> fetchAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(entity -> {
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
                            .categories(new ArrayList<>(entity.getCategories()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    public RestaurantDTO.GetOutputFetchByID fetchRestaurant(Long id){
        Optional<RestaurantEntity> res = restaurantRepository.findById(id);

        if (res.isEmpty()){
            return null;
        }

        RestaurantEntity entity = res.get();

        // 1. Map Menu Photos (extracting just the image URLs)
        List<String> menuPhotos = entity.getMenus().stream()
                .map(menu -> menu.getImgCover())
                .collect(Collectors.toList());

        // 2. Map Reviews
        // DateTimeFormatter handles transforming LocalDateTime to a string like "Maio 2025"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        List<RatingDTO> reviews = entity.getRatings().stream()
                .map(rating -> RatingDTO.builder()
                        .id("r" + rating.getId())
                        .userName(rating.getUser().getUsername())
                        .score(rating.getScore())
                        .createdAt(rating.getCreatedAt() != null ? rating.getCreatedAt().format(formatter) : "")
                        .comment(rating.getComment())
                        .build())
                .collect(Collectors.toList());

        // 3. Calculate Global Average Score
        Double globalScore = reviews.stream()
                .mapToDouble(RatingDTO::getScore)
                .average()
                .orElse(0.0);

        // 4. Map Operating Hours using our helper method below
        List<OpHourDTO> schedule = formatSchedule(entity.getOperatingHours());

        // 5. Build and Return the final Output DTO
        return RestaurantDTO.GetOutputFetchByID.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .phoneNumber(entity.getPhoneNumber())
                .mapsUrl(entity.getMapsUrl())
                .avgPrice(entity.getAvgPrice())
                .logo(entity.getLogo())
                .cover(entity.getCover())
                // Safely grab the city from the address entity
                .city(entity.getAddress() != null ? entity.getAddress().getCity() : null)
                // Round to 1 decimal place (e.g., 4.5)
                .global(Math.round(globalScore * 10.0) / 10.0)
                .menuPhotos(menuPhotos)
                .reviews(reviews)
                .schedule(schedule)
                .build();
    }

    private List<OpHourDTO> formatSchedule(List<OpHourEntity> hours) {
        // Group by the DayOfWeekEntity
        Map<DayEntity, List<OpHourEntity>> groupedHours = hours.stream()
                .collect(Collectors.groupingBy(OpHourEntity::getDay));

        return groupedHours.entrySet().stream()
                // Sort directly by the Day ID (1 to 7) ensuring chronological order
                .sorted(Comparator.comparingInt(entry -> entry.getKey().getId()))
                .map(entry -> {
                    String dayName = entry.getKey().getPtName(); // Directly grabs "Segunda", "Terça", etc.

                    if (entry.getValue().stream().anyMatch(OpHourEntity::getIsClosed)) {
                        return new OpHourDTO(dayName, "Fechado");
                    }

                    List<OpHourEntity> sortedShifts = entry.getValue().stream()
                            .sorted(Comparator.comparing(OpHourEntity::getOpenTime))
                            .collect(Collectors.toList());

                    String hoursString = sortedShifts.stream()
                            .map(h -> h.getOpenTime().toString() + " - " + h.getCloseTime().toString())
                            .collect(Collectors.joining(" | "));

                    return new OpHourDTO(dayName, hoursString);
                })
                .collect(Collectors.toList());
    }
}