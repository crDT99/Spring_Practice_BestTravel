package com.debuggeando_ideas.best_travel.api.controllers;

import com.debuggeando_ideas.best_travel.infrastructure.abstract_services.IFlyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
public class HotelController {
    //inyectamos el servicio
    private final IFlyService flyService;

}
