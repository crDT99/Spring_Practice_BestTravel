package com.debuggeando_ideas.best_travel.infrastructure.helpers;

import com.debuggeando_ideas.best_travel.domain.entities.*;
import com.debuggeando_ideas.best_travel.domain.repositories.ReservationRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.TicketRepository;
import com.debuggeando_ideas.best_travel.infrastructure.services.ReservationService;
import com.debuggeando_ideas.best_travel.infrastructure.services.TicketService;
import com.debuggeando_ideas.best_travel.util.BestTravelUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Transactional
@Component
@AllArgsConstructor
public class TourHelper {

    // este helper ayuda a crear los tickets y las reservaciones
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customer){
        var response = new HashSet<TicketEntity>(flights.size());
        flights.forEach(fly->{
            var ticketToPersist = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer(customer)
                    .price(fly.getPrice().multiply(TicketService.charger_price_percentage))
                    .purchaseDate(LocalDate.now())
                    .arrivalDate(BestTravelUtil.getRandomLatter())
                    .departureDate(BestTravelUtil.getRandomSoon())
                    .build();
            response.add(this.ticketRepository.save(ticketToPersist));
        });
        return response;
    }


    public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer){
        var response = new HashSet<ReservationEntity>(hotels.size());
        hotels.forEach((hotel,totalDays)->{
            var reservationToPersist =
                    ReservationEntity.builder()
                        .id(UUID.randomUUID())
                        .dateTimeReservation(LocalDateTime.now())
                        .dateStart(LocalDate.now())
                        .dateEnd(LocalDate.now().plusDays(totalDays))
                        .totalDays(totalDays)
                        .price(hotel.getPrice().multiply(ReservationService.charger_price_percentage))
                        .hotel(hotel)
                        .customer(customer)
                        .build();
            response.add(this.reservationRepository.save(reservationToPersist));
        });
        return response;
    }


    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer){
        //asd
    }

}
