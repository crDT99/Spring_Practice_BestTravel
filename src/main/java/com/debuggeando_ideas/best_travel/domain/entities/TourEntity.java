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

    // Metodo para eliminar un ticket asociado
    public void removeTicket(UUID id){
        this.tickets.forEach(ticket->{
            if(ticket.getId().equals(id)){
                ticket.setTour(null);
            }
        });
    }

    // Metodo para agregar un nuevo Ticket
    public void addTicket(TicketEntity ticket){
        if(Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
        this.tickets.add(ticket);
        this.tickets.forEach(ticketIteration -> ticketIteration.setTour(this));
    }

    // Metodo para eliminar una Reservacion asociada
    public void removeReservation(UUID id){
        this.reservations.forEach(reservation->{
            if(reservation.getId().equals(id)){
                reservation.setTour(null);
            }
        });
    }

    // Metodo para agregar una nueva Reservacion
    public void addReservation(ReservationEntity reservation){
        if(Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
        this.reservations.add(reservation);
        this.reservations.forEach(reservationIteration -> reservationIteration.setTour(this));
    }



}
