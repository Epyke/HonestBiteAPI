package com.honestbite.www.restaurant.controller;

import com.honestbite.www.restaurant.dto.RestaurantDTO;
import com.honestbite.www.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createRestaurant(
            @RequestParam("data") String dataJson,
            @RequestParam("coverImg") MultipartFile coverImg,
            @RequestParam(value = "menus", required = false) List<MultipartFile> menus) {

        RestaurantDTO.CreateInput input =
                objectMapper.readValue(dataJson, RestaurantDTO.CreateInput.class);

        Long id = restaurantService.createRestaurant(input, coverImg, menus);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO.GetOutputAllRest>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.fetchAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO.GetOutputFetchByID> getRestaurantInfo(@PathVariable("id") Long id){
        RestaurantDTO.GetOutputFetchByID output = restaurantService.fetchRestaurant(id);
        if (output == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(output);
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantDTO.GetOutputAllRest>> getAllRestaurants(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Long categoryId) {

        List<RestaurantDTO.GetOutputAllRest> restaurants = restaurantService.fetchRestaurantsFiltered(name, categoryId);
        return ResponseEntity.ok(restaurants);
    }
}
