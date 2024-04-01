package com.debuggeando_ideas.best_travel.infrastructure.services;

import com.debuggeando_ideas.best_travel.api.models.request.TicketRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debuggeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debuggeando_ideas.best_travel.domain.entities.TicketEntity;
import com.debuggeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.TicketRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.ITicketService;
import com.debuggeando_ideas.best_travel.util.BestTravelUtil;
import com.fasterxml.jackson.databind.util.BeanUtil;
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

public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

// *************************** CRUD ***************************

    // ------------- Create Method -------------
    @Override
    public TicketResponse create(TicketRequest request) {
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();

        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(charger_price_percentage))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();

        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        log.info("Ticket Saved with id:{}", ticketPersisted.getId());

        return this.entityToResponse(ticketPersisted);
    }


    // ------------- Read Method -------------
    @Override
    public TicketResponse read(UUID id) {
        var  ticketFromDB = this.ticketRepository.findById(id).orElseThrow();
        return this.entityToResponse(ticketFromDB);
    }

    // ------------- Update Method -------------

    @Override
    public TicketResponse update(TicketRequest requestUpdate, UUID idUpdate) {

        var ticketToUpdate = ticketRepository.findById(idUpdate).orElseThrow();
        var fly = flyRepository.findById(requestUpdate.getIdFly()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().multiply(charger_price_percentage));
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLatter());
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());

        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);
        log.info("Ticket Updated with id: {}",ticketUpdated.getId());
        return this.entityToResponse(ticketUpdated);
    }

    // ------------- Delete Method -------------
    @Override
    public void delete(UUID idDelete) {
        var ticketToDelete = ticketRepository.findById(idDelete).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);

    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly=this.flyRepository.findById(flyId).orElseThrow();
        return fly.getPrice().multiply(charger_price_percentage);
    }

    //metodo que nos permite cambiar entidades a respuestas
    private TicketResponse entityToResponse(TicketEntity entity){
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity,response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(),flyResponse);
        response.setFly(flyResponse);

        return response;
    }

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(1.25);

}
