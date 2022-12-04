package com.uber.app.team23.AirRide.model.rideData;

//import jakarta.persistence.*;
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
    public Long id;
//    @OneToOne(fetch = FetchType.LAZY)
    public Location startLocation;
//    @OneToOne(fetch = FetchType.LAZY)
    public Location endLocation;
//    @Column(name = "distance")
    public double distance;

    public Route(Long id, Location startLocation, Location endLocation, double distance) {
        this.id = id;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
    }
}
