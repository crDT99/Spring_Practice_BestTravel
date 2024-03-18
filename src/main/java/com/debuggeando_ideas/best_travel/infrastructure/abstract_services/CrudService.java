package com.debuggeando_ideas.best_travel.infrastructure.abstract_services;

public interface CrudService<RQ,RS,ID> {

    //RQ => Request
    //RS => Response
    //ID => Id (varia segun el objeto, puede ser Long , Integer o UUID)

    RS create(RQ request);
    RS read(ID id);
    RS update(RQ requestUpdate,ID idUpdate);
    void delete(ID idDelete);
}
