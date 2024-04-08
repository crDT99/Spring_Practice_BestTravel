package com.debuggeando_ideas.best_travel.infrastructure.services;

import com.debuggeando_ideas.best_travel.api.models.request.TicketRequest;
import com.debuggeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debuggeando_ideas.best_travel.api.models.responses.TicketResponse;
import com.debuggeando_ideas.best_travel.domain.entities.TicketEntity;
import com.debuggeando_ideas.best_travel.domain.repositories.CustomerRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debuggeando_ideas.best_travel.domain.repositories.TicketRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.ITicketService;
import com.debuggeando_ideas.best_travel.infrastructure.helpers.CustomerHelper;
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

// Define the service for managing tickets
@Transactional
@Service
@Slf4j
@AllArgsConstructor

public class TicketService implements ITicketService {

    // Inject the fly, customer, and ticket repositories
    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    // Inject the customer helper
    private final CustomerHelper customerHelper;

    // Define the method for creating a new ticket
    @Override
    public TicketResponse create(TicketRequest request) {
        // Retrieve the fly and customer from the repositories
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();

        // Build the ticket entity to persist
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(charger_price_percentage))
                .purchaseDate(LocalDate.now())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .departureDate(BestTravelUtil.getRandomSoon())
                .build();

        // Save the ticket entity and increase the customer count
        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        log.info("Ticket Saved with id:{}", ticketPersisted.getId());
        customerHelper.increase(customer.getDni(), TicketService.class);

        // Convert the ticket entity to a response and return it
        return this.entityToResponse(ticketPersisted);
    }

    // Define the method for retrieving a ticket by its ID
    @Override
    public TicketResponse read(UUID id) {
        // Retrieve the ticket from the repository
        var  ticketFromDB = this.ticketRepository.findById(id).orElseThrow();

        // Convert the ticket entity to a response and return it
        return this.entityToResponse(ticketFromDB);
    }

    // Define the method for updating a ticket by its ID
    @Override
    public TicketResponse update(TicketRequest requestUpdate, UUID idUpdate) {
        // Retrieve the ticket to update and the fly from the repositories
        var ticketToUpdate = ticketRepository.findById(idUpdate).orElseThrow();
        var fly = flyRepository.findById(requestUpdate.getIdFly()).orElseThrow();

        // Update the ticket details
        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().multiply(charger_price_percentage));
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLatter());
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());

        // Save the updated ticket entity
        var ticketUpdated = this.ticketRepository.save(ticketToUpdate);
        log.info("Ticket Updated with id: {}",ticketUpdated.getId());

        // Convert the ticket entity to a response and return it
        return this.entityToResponse(ticketUpdated);
    }

    // Define the method for deleting a ticket by its ID
    @Override
    public void delete(UUID idDelete) {
        // Retrieve the ticket to delete from the repository
        var ticketToDelete = ticketRepository.findById(idDelete).orElseThrow();

        // Delete the ticket
        this.ticketRepository.delete(ticketToDelete);
    }

    // Define the method for finding the price of a fly
    @Override
    public BigDecimal findPrice(Long flyId) {
        // Retrieve the fly from the repository
        var fly=this.flyRepository.findById(flyId).orElseThrow();

        // Calculate and return the price
        return fly.getPrice().multiply(charger_price_percentage);
    }

    // Define the method for converting a ticket entity to a ticket response
    private TicketResponse entityToResponse(TicketEntity entity){
        var response = new TicketResponse();
        // Copy properties from the entity to the response
        BeanUtils.copyProperties(entity,response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(),flyResponse);
        response.setFly(flyResponse);

        return response;
    }

    // Define the charger price percentage
    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(1.25);

}
