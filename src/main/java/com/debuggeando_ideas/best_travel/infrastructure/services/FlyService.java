package com.debuggeando_ideas.best_travel.infrastructure.services;
// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debuggeando_ideas.best_travel.domain.entities.FlyEntity;
import com.debuggeando_ideas.best_travel.domain.repositories.FlyRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IFlyService;
import com.debuggeando_ideas.best_travel.util.SortType;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

// Define the service for managing flights
@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class FlyService implements IFlyService {

    // Inject the flight repository
    private final FlyRepository flyRepository;

    // Define the method for retrieving all flights
    @Override
    public Page<FlyResponse> readAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        // Determine the sort type
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page,size);
            case LOWER -> pageRequest = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT));
            case UPPER -> pageRequest = PageRequest.of(page,size,Sort.by(FIELD_BY_SORT).descending());
        }
        // Call the repository to retrieve the flights and return the result
        return this.flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    // Define the method for retrieving flights cheaper than a given price
    @Override
    public Set<FlyResponse> readLessPrice(BigDecimal price) {
        // Call the repository to retrieve the flights and return the result
        return this.flyRepository.selectLessPrice(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Define the method for retrieving flights within a price range
    @Override
    public Set<FlyResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        // Call the repository to retrieve the flights and return the result
        return this.flyRepository.selectBetweenPrice(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Define the method for retrieving flights by origin and destination
    @Override
    public Set<FlyResponse> readByOriginDestiny(String origen, String destiny) {
        // Call the repository to retrieve the flights and return the result
        return this.flyRepository.selectOriginDestiny(origen,destiny)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Define the method for converting a flight entity to a flight response
    private FlyResponse entityToResponse(FlyEntity entity){
        FlyResponse response = new FlyResponse();
        // Copy properties from the entity to the response
        BeanUtils.copyProperties(entity,response);
        return  response;
    }

}
