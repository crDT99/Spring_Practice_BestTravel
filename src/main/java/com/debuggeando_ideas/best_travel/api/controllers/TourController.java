package com.debuggeando_ideas.best_travel.api.controllers;


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

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {

    private final ITourService tourService;

    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest request){
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id){
        return ResponseEntity.ok(this.tourService.read(id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //el Put actualiza el objeto completo
    //el Patch actualiza solo una propiedad del objeto
    @PatchMapping(path = "{tourID}/remove_ticket/{ticketID}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long tourID,@PathVariable UUID ticketID){
        this.tourService.removeTicket(tourID,ticketID);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(path = "{tourID}/add_ticket/{flyId}")
    public ResponseEntity<Map<String,UUID>> postTicket(@PathVariable Long tourID, @PathVariable Long flyId){
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourID,flyId));
        return ResponseEntity.ok(response);
    }


    @PatchMapping(path = "{tourID}/remove_reservation/{reservationID}")
    public ResponseEntity<Void> deleteReservarion(@PathVariable Long tourID,@PathVariable UUID reservationID){
        this.tourService.removeReservation(tourID,reservationID);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(path = "{tourID}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String,UUID>> postReservarion(
            @PathVariable Long tourID,
            @PathVariable Long hotelId,
            @RequestParam Integer totalDays
    ){
        var response = Collections.singletonMap("reservationId", this.tourService.addReservation(tourID,hotelId, totalDays));
        return ResponseEntity.ok(response);
    }


}
