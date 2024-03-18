package com.debuggeando_ideas.best_travel.infrastructure.services;

import com.debuggeando_ideas.best_travel.api.models.request.ReservationRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debuggeando_ideas.best_travel.api.models.responses.HotelResponse;
import com.debuggeando_ideas.best_travel.api.models.responses.ReservationResponse;
import com.debuggeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debuggeando_ideas.best_travel.domain.entities.ReservationEntity;
import com.debuggeando_ideas.best_travel.domain.entities.TicketEntity;
import com.debuggeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.HotelRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.ReservationRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IReservationService;
import com.debuggeando_ideas.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService {
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;



    // *************************** CRUD ***************************

    // ------------- Create Method -------------
    @Override
    public ReservationResponse create(ReservationRequest request) {
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow();
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow();

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

        var reservationPersisted = reservationRepository.save(reservationToPersist);

        return this.entityToResponse(reservationPersisted);
    }

    // ------------- Read Method -------------
    @Override
    public ReservationResponse read(UUID id) {
        var  reservationFromDB = this.reservationRepository.findById(id).orElseThrow();
        return this.entityToResponse(reservationFromDB);
    }

    // ------------- Update Method -------------
    @Override
    public ReservationResponse update(ReservationRequest requestUpdate, UUID idUpdate) {
        var hotel = this.hotelRepository.findById(requestUpdate.getIdHotel()).orElseThrow();
        var reservationToUpdate = this.reservationRepository.findById(idUpdate).orElseThrow();


        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(requestUpdate.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(requestUpdate.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().multiply(charger_price_percentage));


        var reservationUpdated = reservationRepository.save(reservationToUpdate);
        log.info("reservation Updated with id: {}",reservationToUpdate.getId());

        return this.entityToResponse(reservationUpdated);
    }

    // ------------- Delete Method -------------
    @Override
    public void delete(UUID idDelete) {
        var reservationToDelete = reservationRepository.findById(idDelete).orElseThrow();
        this.reservationRepository.delete(reservationToDelete);
    }

    //metodo que nos permite cambiar entidades a respuestas
    private ReservationResponse entityToResponse(ReservationEntity entity){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity,response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(),hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }

    @Override
    public BigDecimal findPrice(Long hotelId) {
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow();
        return hotel.getPrice().multiply(charger_price_percentage);
    }

    private static final BigDecimal charger_price_percentage = BigDecimal.valueOf(1.20);

}
