package com.debuggeando_ideas.best_travel.api.controllers;

// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.request.TourRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.TourResponse;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.ITourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

// Define the REST controller for managing tours
@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {

    // Inject the tour service
    private final ITourService tourService;

    // Define the POST endpoint for creating a new tour
    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest request){
        // Call the service to create the tour and return the result
        return ResponseEntity.ok(this.tourService.create(request));
    }

    // Define the GET endpoint for retrieving a tour by its ID
    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id){
        // Call the service to retrieve the tour and return the result
        return ResponseEntity.ok(this.tourService.read(id));
    }

    // Define the DELETE endpoint for deleting a tour by its ID
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        // Call the service to delete the tour
        this.tourService.delete(id);
        // Return no content to indicate successful deletion
        return ResponseEntity.noContent().build();
    }

    // Define the PATCH endpoint for removing a ticket from a tour
    @PatchMapping(path = "{tourID}/remove_ticket/{ticketID}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long tourID,@PathVariable UUID ticketID){
        // Call the service to remove the ticket from the tour
        this.tourService.removeTicket(tourID,ticketID);
        // Return no content to indicate successful removal
        return ResponseEntity.noContent().build();
    }

    // Define the PATCH endpoint for adding a ticket to a tour
    @PatchMapping(path = "{tourID}/add_ticket/{flyId}")
    public ResponseEntity<Map<String,UUID>> postTicket(@PathVariable Long tourID, @PathVariable Long flyId){
        // Call the service to add the ticket to the tour and return the result
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourID,flyId));
        return ResponseEntity.ok(response);
    }

    // Define the PATCH endpoint for removing a reservation from a tour
    @PatchMapping(path = "{tourID}/remove_reservation/{reservationID}")
    public ResponseEntity<Void> deleteReservarion(@PathVariable Long tourID,@PathVariable UUID reservationID){
        // Call the service to remove the reservation from the tour
        this.tourService.removeReservation(tourID,reservationID);
        // Return no content to indicate successful removal
        return ResponseEntity.noContent().build();
    }

    // Define the PATCH endpoint for adding a reservation to a tour
    @PatchMapping(path = "{tourID}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String,UUID>> postReservarion(
            @PathVariable Long tourID,
            @PathVariable Long hotelId,
            @RequestParam Integer totalDays
    ){
        // Call the service to add the reservation to the tour and return the result
        var response = Collections.singletonMap("reservationId", this.tourService.addReservation(tourID,hotelId, totalDays));
        return ResponseEntity.ok(response);
    }
}
