package com.debuggeando_ideas.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name="tour")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourEntity  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "tour",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<ReservationEntity> reservations;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "tour",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<TicketEntity> tickets;

    @ManyToOne
    @JoinColumn(name="id_customer")
    private CustomerEntity customer;

    // Mapeando relaciones inversas  -- De Ticket
    @PrePersist
    @PreRemove
    public void updateFk(){
        this.reservations.forEach(res -> res.setTour(this));
        this.tickets.forEach(ticket -> ticket.setTour(this));
    }

    // metodo para eliminar un ticket asociado
    public void removeTicket(UUID id){
        this.tickets.forEach(ticket->{
            if(ticket.getId().equals(id)){
                ticket.setTour(null);
            }
        });
    }

}
