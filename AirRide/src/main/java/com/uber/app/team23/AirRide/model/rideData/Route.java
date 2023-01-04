package com.uber.app.team23.AirRide.model.rideData;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "routes")
public class Route {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure", referencedColumnName = "id")
    private Location departure;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination", referencedColumnName = "id")
    private Location destination;
    @Column(name = "distance")
    @JsonIgnore
    private double distance;

}
