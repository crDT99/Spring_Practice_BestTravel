package com.debuggeando_ideas.best_travel.infrastructure.services;

// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.request.ReservationRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.HotelResponse;
import com.debuggeando_ideas.best_travel.api.models.responses.ReservationResponse;
import com.debuggeando_ideas.best_travel.domain.entities.ReservationEntity;
import com.debuggeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.HotelRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.ReservationRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IReservationService;
import com.debuggeando_ideas.best_travel.infrastructure.helpers.CustomerHelper;
import com.debuggeando_ideas.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

// Define the service for managing reservations
@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {

    // Inject the hotel, customer, and reservation repositories
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    // Inject the customer helper
    private final CustomerHelper customerHelper;

    // Define the method for creating a new reservation
    @Override
    public ReservationResponse create(ReservationRequest request) {
        // Retrieve the hotel and customer from the repositories
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(()-> new IdNotFoundException("hotel"));
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow(()-> new IdNotFoundException("customer"));

        // Build the reservation entity to persist
        var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .totalDays(request.getTotalDays())
                .price(hotel.getPrice().multiply(charger_price_percentage))
                .hotel(hotel)
                .customer(customer)
                .build();

        // Save the reservation entity and increase the customer count
        var reservationPersisted = reservationRepository.save(reservationToPersist);
        this.customerHelper.increase(customer.getDni(), ReservationService.class);

        // Convert the reservation entity to a response and return it
        return this.entityToResponse(reservationPersisted);
    }

    // Define the method for retrieving a reservation by its ID
    @Override
    public ReservationResponse read(UUID id) {
        // Retrieve the reservation from the repository
        var  reservationFromDB = this.reservationRepository.findById(id).orElseThrow(()-> new IdNotFoundException("reservation"));

        // Convert the reservation entity to a response and return it
        return this.entityToResponse(reservationFromDB);
    }

    // Define the method for updating a reservation by its ID
    @Override
    public ReservationResponse update(ReservationRequest requestUpdate, UUID idUpdate) {
        // Retrieve the hotel and reservation to update from the repositories
        var hotel = this.hotelRepository.findById(requestUpdate.getIdHotel()).orElseThrow(()-> new IdNotFoundException("hotel"));
        var reservationToUpdate = this.reservationRepository.findById(idUpdate).orElseThrow(()-> new IdNotFoundException("reservation"));

        // Update the reservation details
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(requestUpdate.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(requestUpdate.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().multiply(charger_price_percentage));

        // Save the updated reservation entity
        var reservationUpdated = reservationRepository.save(reservationToUpdate);

        // Log the update and convert the reservation entity to a response and return it
        log.info("reservation Updated with id: {}",reservationToUpdate.getId());
        return this.entityToResponse(reservationUpdated);
    }

    // Define the method for deleting a reservation by its ID
    @Override
    public void delete(UUID idDelete) {
        // Retrieve the reservation to delete from the repository
        var reservationToDelete = reservationRepository.findById(idDelete).orElseThrow(()-> new IdNotFoundException("reservation"));

        // Delete the reservation
        this.reservationRepository.delete(reservationToDelete);
    }

    // Define the method for converting a reservation entity to a reservation response
    private ReservationResponse entityToResponse(ReservationEntity entity){
        var response = new ReservationResponse();
        // Copy properties from the entity to the response
        BeanUtils.copyProperties(entity,response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(),hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }

    // Define the method for finding the price of a hotel
    @Override
    public BigDecimal findPrice(Long hotelId) {
        // Retrieve the hotel from the repository
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(()-> new IdNotFoundException("hotel"));

        // Calculate and return the price
        return hotel.getPrice().multiply(charger_price_percentage);
    }

    // Define the charger price percentage
    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(1.20);

}
