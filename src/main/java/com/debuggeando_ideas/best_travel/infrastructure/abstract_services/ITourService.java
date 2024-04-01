package com.debuggeando_ideas.best_travel.infrastructure.abstract_services;

import com.debuggeando_ideas.best_travel.api.models.request.TourRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.TourResponse;

import java.util.UUID;

public interface ITourService extends SimpleCRUDService<TourRequest, TourResponse , Long>{

// manage flight tickets
    void removeTicket(UUID ticketID, Long tourID);
    UUID addTicket(Long flyId, Long tourID);

    // manage reservations
    void removeReservation(UUID reservationID, Long tourID);
    UUID addReservation(Long reservationID, Long tourID);


}
