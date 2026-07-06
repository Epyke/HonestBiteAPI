package com.honestbite.www.rating.controller;

import com.honestbite.www.rating.dto.RatingDTO;
import com.honestbite.www.rating.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingDTO.PostOutput> createRating(
            @RequestBody RatingDTO.PostInput input,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(ratingService.saveRating(input, userDetails.getUsername()));
    }
}