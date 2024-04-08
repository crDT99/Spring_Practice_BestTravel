package com.debuggeando_ideas.best_travel.api.controllers;

// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IFlyService;
import com.debuggeando_ideas.best_travel.util.SortType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

// Define the REST controller for managing flights
@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
public class FlyController {

    // Inject the flight service
    private final IFlyService flyService;

    // Define the GET endpoint for retrieving all flights
    @GetMapping
    public ResponseEntity<Page<FlyResponse>>getAll(
            @RequestParam Integer page,  // The page number to retrieve
            @RequestParam Integer size,  // The number of records per page
            @RequestHeader(required = false) SortType sortType){  // The sorting type

        // If no sort type is provided, default to NONE
        if(Objects.isNull(sortType))sortType= SortType.NONE;

        // Call the service to retrieve the flights
        var response = this.flyService.readAll(page,size,sortType);

        // Return the flights, or no content if none found
        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    // Define the GET endpoint for retrieving flights cheaper than a given price
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>>getLessPrice(@RequestParam BigDecimal price){
        // Call the service to retrieve the flights
        var response = this.flyService.readLessPrice(price);

        // Return the flights, or no content if none found
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    // Define the GET endpoint for retrieving flights within a price range
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>>getBetweenPrice(
            @RequestParam BigDecimal price_min,  // The minimum price
            @RequestParam BigDecimal price_max  // The maximum price
    ){
        // Call the service to retrieve the flights
        var response = this.flyService.readBetweenPrices(price_min,price_max);

        // Return the flights, or no content if none found
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    // Define the GET endpoint for retrieving flights between two locations
    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>>getByOriginDestiny(
            @RequestParam String origin,  // The origin location
            @RequestParam String destiny  // The destination location
    ){
        // Call the service to retrieve the flights
        var response = this.flyService.readByOriginDestiny(origin,destiny);

        // Return the flights, or no content if none found
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}