package com.debuggeando_ideas.best_travel.api.controllers;

import com.debuggeando_ideas.best_travel.api.models.responses.FlyResponse;
import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IFlyService;
import com.debuggeando_ideas.best_travel.util.SortType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
public class FlyController {

    private final IFlyService flyService;

    @GetMapping
    public ResponseEntity<Page<FlyResponse>>getAll(
            @RequestParam Integer page,  //pagina
            @RequestParam Integer size,  //tamaño de la pagina
            @RequestHeader(required = false) SortType sortType){

        if(Objects.isNull(sortType))sortType= SortType.NONE;
        var response = this.flyService.readAll(page,size,sortType);

        return response.isEmpty()? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>>getLessPrice(@RequestParam BigDecimal price){
        var response = this.flyService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }


    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>>getBetweenPrice(
            @RequestParam BigDecimal price_min,
            @RequestParam BigDecimal price_max
    ){

        var response = this.flyService.readBetweenPrices(price_min,price_max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>>getByOriginDestiny(
            @RequestParam String origin,
            @RequestParam String destiny
    ){
        var response = this.flyService.readByOriginDestiny(origin,destiny);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
