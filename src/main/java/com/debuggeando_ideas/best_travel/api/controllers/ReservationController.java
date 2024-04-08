package com.debuggeando_ideas.best_travel.api.controllers;

import com.debuggeando_ideas.best_travel.api.models.request.ReservationRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.ReservationResponse;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

// Define the REST controller for managing reservations
@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
public class ReservationController {
    // Inject the reservation service
    private final IReservationService reservationService;

    // Define the POST endpoint for creating a new reservation
    @PostMapping
    public ResponseEntity<ReservationResponse>post(@RequestBody ReservationRequest request){
        // Call the service to create the reservation and return the result
        return ResponseEntity.ok(reservationService.create(request));
    }

    // Define the GET endpoint for retrieving a reservation by its ID
    @GetMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> get(@PathVariable UUID id){
        // Call the service to retrieve the reservation and return the result
        return ResponseEntity.ok(reservationService.read(id));
    }

    // Define the PUT endpoint for updating a reservation by its ID
    @PutMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> put(@PathVariable UUID id , @RequestBody ReservationRequest request){
        // Call the service to update the reservation and return the result
        return ResponseEntity.ok(this.reservationService.update(request,id));
    }

    // Define the DELETE endpoint for deleting a reservation by its ID
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        // Call the service to delete the reservation
        this.reservationService.delete(id);
        // Return no content to indicate successful deletion
        return ResponseEntity.noContent().build();
    }

    // Define the GET endpoint for retrieving the price of a reservation by its ID
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getReservationPrice(@RequestParam Long reservationId){
        // Call the service to find the price of the reservation and return the result
        return ResponseEntity.ok(Collections.singletonMap("ticketPrice", this.reservationService.findPrice(reservationId)));
    }
}
