package com.debuggeando_ideas.best_travel.domain.entities;

import com.debuggeando_ideas.best_travel.util.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name="fly")
//-------- creacion de constructores mediante Lombock --------

@NoArgsConstructor  //Constructor vacio
@AllArgsConstructor //Constructor con todos los argumentos

//-------- creacion de metodos de la clase mediante Lombock --------
@Data // lombock crea getters, setters , toString, equals y hashCode

// patron de diseño builder evita tener sobrecarga de constructores
@Builder

// -------------------- Clase --------------------
public class FlyEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //big serial se mapea como long

    // el tipo decimal se mapea como Double
    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;

    @Column(length = 20)
    private String originName;
    @Column(length = 20)
    private String destinyName;


    // lo mejor para manejar dinero es BigDecimal
    // double precision se mapea como BigDecimal
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "fly",
            cascade = CascadeType.ALL, //las operaciones se hacen en cascada
            fetch = FetchType.EAGER,
            // Lazy :select * from fly
            // Eager: select * from fly  f join ticket t on f.id = t.fly_id
            orphanRemoval = true
            // elimina a los registros que no tienen relacion

    )
    private Set<TicketEntity>tickets;

}
