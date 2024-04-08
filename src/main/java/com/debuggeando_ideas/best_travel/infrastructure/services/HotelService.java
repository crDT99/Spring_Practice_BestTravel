package com.debuggeando_ideas.best_travel.infrastructure.services;

// Import necessary libraries and dependencies
import com.debuggeando_ideas.best_travel.api.models.responses.HotelResponse;
import com.debuggeando_ideas.best_travel.domain.entities.HotelEntity;
import com.debuggeando_ideas.best_travel.domain.repositories.HotelRepository;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IHotelService;
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

// Define the service for managing hotels
@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class HotelService implements IHotelService {

    // Inject the hotel repository
    private final HotelRepository hotelRepository;

    // Define the method for retrieving all hotels
    @Override
    public Page<HotelResponse> readAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        // Determine the sort type
        switch (sortType){
            case NONE -> pageRequest = PageRequest.of(page,size);
            case LOWER -> pageRequest = PageRequest.of(page,size, Sort.by(FIELD_BY_SORT));
            case UPPER -> pageRequest = PageRequest.of(page,size,Sort.by(FIELD_BY_SORT).descending());
        }
        // Call the repository to retrieve the hotels and return the result
        return this.hotelRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    // Define the method for retrieving hotels cheaper than a given price
    @Override
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        // Call the repository to retrieve the hotels and return the result
        return this.hotelRepository.findByPriceLessThan(price)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Define the method for retrieving hotels within a price range
    @Override
    public Set<HotelResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        // Call the repository to retrieve the hotels and return the result
        return this.hotelRepository.findByPriceBetween(min, max)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Define the method for retrieving hotels by rating
    @Override
    public Set<HotelResponse> readByRating(Integer rating) {
        // Call the repository to retrieve the hotels and return the result
        return this.hotelRepository.findByRatingGreaterThan(rating)
                .stream()
                .map(this::entityToResponse)
                .collect(Collectors.toSet());
    }

    // Define the method for converting a hotel entity to a hotel response
    private HotelResponse entityToResponse(HotelEntity entity){
        HotelResponse response = new HotelResponse();
        // Copy properties from the entity to the response
        BeanUtils.copyProperties(entity,response);
        return  response;
    }

}

