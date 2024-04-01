package com.debuggeando_ideas.best_travel.infrastructure.abstract_services;

//Generic Interface
public interface SimpleCRUDService<RQ,RS,ID> {
    RS create(RQ request);
    RS read(ID id);
    void delete(ID idDelete);
}
