package com.uber.app.team23.AirRide.model.rideData;

//import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@NoArgsConstructor
//@Table(name = "Routes")
public class Route {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long id;
//    @OneToOne(fetch = FetchType.LAZY)
    public Location departure;
//    @OneToOne(fetch = FetchType.LAZY)
    public Location destination;
//    @Column(name = "distance")
    @JsonIgnore
    public double distance;

    public Route(Long id, Location departure, Location destination, double distance) {
        this.id = id;
        this.departure = departure;
        this.destination = destination;
        this.distance = distance;
    }
}
