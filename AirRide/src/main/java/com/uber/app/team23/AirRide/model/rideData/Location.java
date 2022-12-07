package com.uber.app.team23.AirRide.model.rideData;


//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@Entity
//@Table(name = "Location")
public class Location {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
//    @Column(name = "longitude", nullable = false)
    public double longitude;
//    @Column(name = "latitude", nullable = false)
    public double latitude;
//    @Column(name = "address")
    public String address;


    public Location(Long id, double longitude, double latitude, String address) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
    }

}

