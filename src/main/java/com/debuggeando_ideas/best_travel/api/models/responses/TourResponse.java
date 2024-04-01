package com.debuggeando_ideas.best_travel.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourResponse implements Serializable {
    //que se responde cuando se cree un Tour?
    private Long id; // id del tour - generado por la DB
    private Set<UUID> ticketIDs; // set de UUID's de los tickets que se crearon
    private Set<UUID> reservationIDs; // set de UUID's de las reservaciones que se crearon


}
