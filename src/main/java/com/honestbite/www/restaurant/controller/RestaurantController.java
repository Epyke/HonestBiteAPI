package com.honestbite.www.restaurant.controller;

import com.honestbite.www.restaurant.dto.RestaurantDTO;
import com.honestbite.www.restaurant.model.RestaurantEntity;
import com.honestbite.www.restaurant.persistence.RestaurantRepository;
import com.honestbite.www.restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;

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
}
