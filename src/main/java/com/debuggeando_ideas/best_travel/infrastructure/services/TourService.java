package com.debuggeando_ideas.best_travel.infrastructure.services;

import com.debuggeando_ideas.best_travel.api.models.request.TourRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.TourResponse;
import com.debuggeando_ideas.best_travel.domain.entities.*;
import com.debuggeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.HotelRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.TourRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.ITourService;
import com.debuggeando_ideas.best_travel.infrastructure.helpers.TourHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
@AllArgsConstructor
public class TourService implements ITourService {

    // inyectamos lo que necesitamos para el servicio
    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;

    //para evitar crear un servicio dentro de otro usamos un helper
    private final TourHelper tourHelper;


    @Override
    public TourResponse create(TourRequest request) {
        var customer = customerRepository.findById(request.getCustomerID()).orElseThrow();
        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> flights.add(this.flyRepository.findById(fly.getId()).orElseThrow()));
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(hotel.getId()).orElseThrow() , hotel.getTotalDays()));

        var tourToSave = TourEntity.builder()
                .tickets(this.tourHelper.createTickets(flights,customer))
                .reservations(this.tourHelper.createReservations(hotels,customer))
                .customer(customer)
                .build();
        var tourSaved = this.tourRepository.save(tourToSave);


        return TourResponse.builder()
                .reservationIDs(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIDs(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourSaved.getId())
                .build();
    }

    @Override
    public TourResponse read(Long id) {
       var tourFromDB = this.tourRepository.findById(id).orElseThrow();
        return TourResponse.builder()
                .reservationIDs(tourFromDB.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIDs(tourFromDB.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourFromDB.getId())
                .build();
    }

    @Override
    public void delete(Long idDelete) {
        var tourToDelete = this.tourRepository.findById(idDelete).orElseThrow();
        this.tourRepository.delete(tourToDelete);

    }

    @Override
    public void removeTicket(UUID ticketID, Long tourID) {
        var tourToUpdate = tourRepository.findById(tourID).orElseThrow();
        tourToUpdate.removeTicket(ticketID);
        this.tourRepository.save(tourToUpdate);
    }

    @Override
    public UUID addTicket(Long flyId, Long tourID) {
        var tourToUpdate = tourRepository.findById(tourID).orElseThrow();
        var fly = this.flyRepository.findById(flyId).orElseThrow();

        return null;
    }

    @Override
    public void removeReservation(UUID reservationID, Long tourID) {

    }

    @Override
    public UUID addReservation(Long reservationID, Long tourID) {
        return null;
    }

}
