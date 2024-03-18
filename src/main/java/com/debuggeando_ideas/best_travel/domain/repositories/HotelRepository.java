package com.debuggeando_ideas.best_travel.domain.repositories;

import com.debuggeando_ideas.best_travel.domain.entities.FlyEntity;
import com.debuggeando_ideas.best_travel.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface HotelRepository extends JpaRepository<HotelEntity,Long> {

    //https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html


    @Query("Select h from hotel h where h.price < :price")
    Set<HotelEntity> findByPriceLessThan(BigDecimal price);

    @Query("Select h from hotel h where h.price Between :min and :max")
    Set<HotelEntity> findByPriceBetween(BigDecimal min , BigDecimal max);

    @Query("Select h from hotel h where h.rating > :rating")
    Set<HotelEntity> findByRatingGreaterThan(Integer rating);

    @Query("Select h from hotel h join fetch reservation r where r.id = :id")
    Optional<HotelEntity> findByReservationId(UUID id);

}
