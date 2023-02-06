package com.uber.app.team23.AirRide.model.rideData;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "routes")
public class Route {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "departure", referencedColumnName = "id")
    @NotNull
    private Location departure;

    @ManyToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "destination", referencedColumnName = "id")
    @NotNull
    private Location destination;

    @Column(name = "distance")
    @JsonIgnore
    private double distance;

    public Route(Location departure, Location destination, double distance) {
        this.departure = departure;
        this.destination = destination;
        this.distance = distance;
    }
}
