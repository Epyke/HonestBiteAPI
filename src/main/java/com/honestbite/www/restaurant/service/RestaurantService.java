package com.honestbite.www.restaurant.service;

import com.honestbite.www.adress.dto.AdressDTO;
import com.honestbite.www.adress.model.AdressEntity;
import com.honestbite.www.category.model.CategoryEntity;
import com.honestbite.www.category.persistence.CategoryRepository;
import com.honestbite.www.cloudinary.CloudinaryService;
import com.honestbite.www.days.model.DayEntity;
import com.honestbite.www.days.persistence.DayRepository;
import com.honestbite.www.menu.model.MenuEntity;
import com.honestbite.www.menu.persistence.MenuRepository;
import com.honestbite.www.ophour.persistence.OpHourRepository;
import com.honestbite.www.ophour.dto.OpHourDTO;
import com.honestbite.www.rating.dto.RatingDTO;
import com.honestbite.www.restaurant.dto.RestaurantDTO;
import com.honestbite.www.restaurant.model.RestaurantEntity;
import com.honestbite.www.restaurant.persistence.RestaurantRepository;
import com.honestbite.www.ophour.model.OpHourEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DayRepository dayRepository;

    @Autowired
    private OpHourRepository opHourRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Transactional
    public Long createRestaurant(RestaurantDTO.CreateInput in,
                                 MultipartFile coverImg,
                                 List<MultipartFile> menus) {

        // 1. Address (cascade ALL on the relation persists it with the restaurant)
        AdressDTO.AddressInput a = in.getAddress();
        AdressEntity address = AdressEntity.builder()
                .street(a.getStreet())
                .city(a.getCity())
                .build();

        // 2. Categories (must already exist)
        Set<CategoryEntity> categories = new HashSet<>();
        if (in.getCategoryIds() != null && !in.getCategoryIds().isEmpty()) {
            categories = new HashSet<>(categoryRepository.findAllById(in.getCategoryIds()));
        }

        // 3. Cover image -> Cloudinary, store the returned URL
        String coverUrl = cloudinaryService.upload(coverImg, "restaurants/covers");

        // 4. Restaurant
        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(in.getName())
                .description(in.getDescription())
                .phoneNumber(in.getPhoneNumber())
                .avgPrice(in.getAvgPrice())
                .mapsUrl(in.getMapsUrl())
                .cover(coverUrl)
                .address(address)
                .categories(categories)
                .build();

        RestaurantEntity saved = restaurantRepository.save(restaurant);

        // 5. Menu images -> Cloudinary, one MenuEntity per file
        if (menus != null) {
            for (MultipartFile file : menus) {
                String url = cloudinaryService.upload(file, "restaurants/menus");
                MenuEntity menu = MenuEntity.builder()
                        .imgCover(url)
                        .isAvailable(true)
                        .restaurant(saved)
                        .build();
                menuRepository.save(menu);
            }
        }

        // 6. Operating hours -> one OpHourEntity per interval (or one "closed" row per closed day)
        if (in.getOpHours() != null) {
            for (RestaurantDTO.OpHourInput dayInput : in.getOpHours()) {
                DayEntity day = dayRepository.findByPtName(dayInput.getDay()).orElse(null);
                if (day == null) {
                    continue; // unknown day label -> skip rather than fail the whole create
                }

                boolean closed = Boolean.TRUE.equals(dayInput.getIsClosed())
                        || dayInput.getIntervals() == null
                        || dayInput.getIntervals().isEmpty();

                if (closed) {
                    opHourRepository.save(OpHourEntity.builder()
                            .day(day)
                            .restaurant(saved)
                            .isClosed(true)
                            .build());
                    continue;
                }

                for (RestaurantDTO.IntervalInput interval : dayInput.getIntervals()) {
                    opHourRepository.save(OpHourEntity.builder()
                            .day(day)
                            .restaurant(saved)
                            .isClosed(false)
                            .openTime(LocalTime.parse(interval.getOpen()))
                            .closeTime(LocalTime.parse(interval.getClose()))
                            .build());
                }
            }
        }

        return saved.getId();
    }

    public List<RestaurantDTO.GetOutputAllRest> fetchAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(entity -> {
                    Double globalScore = entity.getRatings().stream()
                            .mapToDouble(rating -> rating.getScore())
                            .average()
                            .orElse(5.0);

                    return RestaurantDTO.GetOutputAllRest.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .cover(entity.getCover())
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
        List<RatingDTO.DisplayReview> reviews = entity.getRatings().stream()
                .map(rating -> RatingDTO.DisplayReview.builder()
                        .id(rating.getId())
                        .username(rating.getUser().getUsername())
                        .score(rating.getScore())
                        .createdAt(rating.getCreatedAt() != null ? rating.getCreatedAt().format(formatter) : "")
                        .comment(rating.getComment())
                        .build())
                .collect(Collectors.toList());

        Double globalScore = reviews.stream()
                .mapToDouble(RatingDTO.DisplayReview::getScore) // FIXED: Map directly from your clean DisplayReview items list
                .average()
                .orElse(5.0);

        // 4. Map Operating Hours using our helper method below
        List<OpHourDTO> schedule = formatSchedule(entity.getOperatingHours());

        // 4b. Map Categories
        List<com.honestbite.www.category.dto.CategoryDTO.GetOutput> categories = entity.getCategories().stream()
                .map(cat -> com.honestbite.www.category.dto.CategoryDTO.GetOutput.builder()
                        .id(cat.getId())
                        .label(cat.getLabel())
                        .value(cat.getValue())
                        .color(cat.getColor())
                        .build())
                .collect(Collectors.toList());

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
                // Safely grab the street/city from the address entity
                .street(entity.getAddress() != null ? entity.getAddress().getStreet() : null)
                .city(entity.getAddress() != null ? entity.getAddress().getCity() : null)
                // Round to 1 decimal place (e.g., 4.5)
                .global(Math.round(globalScore * 10.0) / 10.0)
                .menuPhotos(menuPhotos)
                .reviews(reviews)
                .schedule(schedule)
                .categories(categories)
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

    public List<RestaurantDTO.GetOutputAllRest> fetchRestaurantsFiltered(String name, Long categoryId) {
        return restaurantRepository.findAll().stream()
                // Filter 1: Check by text string matching name
                .filter(entity -> {
                    if (name != null && !name.isBlank()) {
                        return entity.getName().toLowerCase().contains(name.toLowerCase());
                    }
                    return true;
                })
                // Filter 2: Check by Category Primary Key ID
                .filter(entity -> {
                    if (categoryId != null) {
                        // Assumes entity.getCategories() returns a list of Category entities
                        return entity.getCategories().stream()
                                .anyMatch(category -> category.getId().equals(categoryId));
                    }
                    return true;
                })
                // Map the results back to the target list format
                .map(entity -> {
                    Double globalScore = entity.getRatings().stream()
                            .mapToDouble(rating -> rating.getScore())
                            .average()
                            .orElse(5.0);

                    return RestaurantDTO.GetOutputAllRest.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .cover(entity.getCover())
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
                })
                .collect(Collectors.toList());
    }
}