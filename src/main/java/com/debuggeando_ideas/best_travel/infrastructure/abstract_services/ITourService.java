package com.debuggeando_ideas.best_travel.infrastructure.abstract_services;

import com.debuggeando_ideas.best_travel.api.models.request.TourRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCRUDService<TourRequest, TourResponse , Long>{

// manage flight tickets
    void removeTicket(Long tourID, UUID ticketID );
    UUID addTicket(Long tourID, Long flyId);

    // manage reservations
    void removeReservation(Long tourID,  UUID reservationID);
    UUID addReservation(Long reservationID, Long tourID, Integer totalDays);


}
