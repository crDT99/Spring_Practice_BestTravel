package com.debuggeando_ideas.best_travel.api.controllers;

// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.request.TicketRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.ITicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

// Define the REST controller for managing tickets
@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
public class TicketController {

    // Inject the ticket service
    private final ITicketService ticketService;

    // Define the POST endpoint for creating a new ticket
    @PostMapping
    public ResponseEntity<TicketResponse> post(@RequestBody TicketRequest request){
        // Call the service to create the ticket and return the result
        return ResponseEntity.ok(ticketService.create(request));
    }

    // Define the GET endpoint for retrieving a ticket by its ID
    @GetMapping(path = "{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id){
        // Call the service to retrieve the ticket and return the result
        return ResponseEntity.ok(ticketService.read(id));
    }

    // Define the PUT endpoint for updating a ticket by its ID
    @PutMapping(path = "{id}")
    public ResponseEntity<TicketResponse> put(@PathVariable UUID id , @RequestBody TicketRequest request){
        // Call the service to update the ticket and return the result
        return ResponseEntity.ok(this.ticketService.update(request,id));
    }

    // Define the DELETE endpoint for deleting a ticket by its ID
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        // Call the service to delete the ticket
        this.ticketService.delete(id);
        // Return no content to indicate successful deletion
        return ResponseEntity.noContent().build();
    }

    // Define the GET endpoint for retrieving the price of a flight by its ID
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId){
        // Call the service to find the price of the flight and return the result
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice(flyId)));
    }
}
