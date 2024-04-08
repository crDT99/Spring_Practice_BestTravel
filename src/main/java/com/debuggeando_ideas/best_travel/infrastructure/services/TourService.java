package com.debuggeando_ideas.best_travel.infrastructure.services;

import com.debuggeando_ideas.best_travel.api.models.request.TourRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.TourResponse;
import com.debuggeando_ideas.best_travel.domain.entities.*;
import com.debuggeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.HotelRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.TourRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.ITourService;
import com.debuggeando_ideas.best_travel.infrastructure.helpers.CustomerHelper;
import com.debuggeando_ideas.best_travel.infrastructure.helpers.TourHelper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

// Define the service for managing tours
@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class TourService implements ITourService {

    // Inject the tour, fly, hotel, and customer repositories
    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;

    // Inject the tour and customer helpers
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;

    // Define the method for creating a new tour
    @Override
    public TourResponse create(TourRequest request) {
        // Retrieve the customer from the repository
        var customer = customerRepository.findById(request.getCustomerID()).orElseThrow();

        // Initialize the flights and hotels
        var flights = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> flights.add(this.flyRepository.findById(fly.getId()).orElseThrow()));
        var hotels = new HashMap<HotelEntity, Integer>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(hotel.getId()).orElseThrow() , hotel.getTotalDays()));

        // Build the tour entity to persist
        var tourToSave = TourEntity.builder()
                .tickets(this.tourHelper.createTickets(flights,customer))
                .reservations(this.tourHelper.createReservations(hotels,customer))
                .customer(customer)
                .build();

        // Save the tour entity and increase the customer count
        var tourSaved = this.tourRepository.save(tourToSave);
        this.customerHelper.increase(customer.getDni(), TourService.class);

        // Convert the tour entity to a response and return it
        return TourResponse.builder()
                .reservationIDs(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIDs(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourSaved.getId())
                .build();
    }

    // Define the method for retrieving a tour by its ID
    @Override
    public TourResponse read(Long id) {
        // Retrieve the tour from the repository
        var tourFromDB = this.tourRepository.findById(id).orElseThrow();

        // Convert the tour entity to a response and return it
        return TourResponse.builder()
                .reservationIDs(tourFromDB.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIDs(tourFromDB.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .id(tourFromDB.getId())
                .build();
    }

    // Define the method for deleting a tour by its ID
    @Override
    public void delete(Long idDelete) {
        // Retrieve the tour to delete from the repository
        var tourToDelete = this.tourRepository.findById(idDelete).orElseThrow();

        // Delete the tour
        this.tourRepository.delete(tourToDelete);
    }

    // Define the method for removing a ticket from a tour
    @Override
    public void removeTicket(Long tourID,UUID ticketID) {
        // Retrieve the tour to update from the repository
        var tourToUpdate = tourRepository.findById(tourID).orElseThrow();

        // Remove the ticket from the tour
        tourToUpdate.removeTicket(ticketID);

        // Save the updated tour entity
        this.tourRepository.save(tourToUpdate);
    }

    // Define the method for adding a ticket to a tour
    @Override
    public UUID addTicket(Long tourID, Long flyId) {
        // Retrieve the tour to update and the fly from the repositories
        var tourToUpdate = tourRepository.findById(tourID).orElseThrow();
        var fly = this.flyRepository.findById(flyId).orElseThrow();

        // Create the ticket and add it to the tour
        var ticket = this.tourHelper.createTicket(fly,tourToUpdate.getCustomer());
        tourToUpdate.addTicket(ticket);

        // Save the updated tour entity
        this.tourRepository.save(tourToUpdate);

        // Return the ID of the ticket
        return ticket.getId();
    }

    // Define the method for removing a reservation from a tour
    @Override
    public void removeReservation(Long tourID, UUID reservationID) {
        // Retrieve the tour to update from the repository
        var tourToUpdate = tourRepository.findById(tourID).orElseThrow();

        // Remove the reservation from the tour
        tourToUpdate.removeReservation(reservationID);

        // Save the updated tour entity
        this.tourRepository.save(tourToUpdate);
    }

    // Define the method for adding a reservation to a tour
    @Override
    public UUID addReservation(Long tourID, Long hotelID , Integer totalDays) {
        // Retrieve the tour to update and the hotel from the repositories
        var tourToUpdate = tourRepository.findById(tourID).orElseThrow();
        var hotel = this.hotelRepository.findById(hotelID).orElseThrow();

        // Create the reservation and add it to the tour
        var reservation = this.tourHelper.createReservation(hotel,tourToUpdate.getCustomer(),totalDays);
        tourToUpdate.addReservation(reservation);

        // Save the updated tour entity
        this.tourRepository.save(tourToUpdate);

        // Return the ID of the reservation
        return reservation.getId();
    }

}
