package com.debuggeando_ideas.best_travel.infrastructure.abstract_services;

import com.debuggeando_ideas.best_travel.util.SortType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Set;

public interface CatalogService<Resp> {
    Page<Resp> readAll(Integer page, Integer size, SortType sortType);
    Set<Resp> readLessPrice(BigDecimal price);
    Set<Resp> readBetweenPrices(BigDecimal min , BigDecimal max);

    String FIELD_BY_SORT = "price";

}
