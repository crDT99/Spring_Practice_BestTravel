package com.debuggeando_ideas.best_travel.api.controllers;

// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.responses.HotelResponse;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IHotelService;
import com.debuggeando_ideas.best_travel.util.SortType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

// Define the REST controller for managing hotels
@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
public class HotelController {
    // Inject the hotel service
    private final IHotelService hotelService;

    // Define the GET endpoint for retrieving all hotels
    @GetMapping
    public ResponseEntity<Page<HotelResponse>> getAll(
            @RequestParam Integer page,  // The page number to retrieve
            @RequestParam Integer size,  // The number of records per page
            @RequestHeader(required = false) SortType sortType){  // The sorting type

        // If no sort type is provided, default to NONE
        if(Objects.isNull(sortType)) sortType= SortType.NONE;

        // Call the service to retrieve the hotels
        var response = this.hotelService.readAll(page,size,sortType);

        // Return the hotels, or no content if none found
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    // Define the GET endpoint for retrieving hotels cheaper than a given price
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(@RequestParam BigDecimal price){
        // Call the service to retrieve the hotels
        var response = this.hotelService.readLessPrice(price);

        // Return the hotels, or no content if none found
        return  response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    // Define the GET endpoint for retrieving hotels within a price range
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<HotelResponse>>getBetweenPrice(@RequestParam BigDecimal min, @RequestParam BigDecimal max){
        // Call the service to retrieve the hotels
        var response = this.hotelService.readBetweenPrices(min, max);

        // Return the hotels, or no content if none found
        return  response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    // Define the GET endpoint for retrieving hotels by rating
    @GetMapping(path = "rating")
    public ResponseEntity<Set<HotelResponse>>getByRating(@RequestParam Integer rating){
        // Ensure the rating is between 1 and 4
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;

        // Call the service to retrieve the hotels
        var response = this.hotelService.readByRating(rating);

        // Return the hotels, or no content if none found
        return  response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }
}
