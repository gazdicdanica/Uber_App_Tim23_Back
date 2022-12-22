package com.uber.app.team23.AirRide.model.rideData;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "locations")
public class Location {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "longitude", nullable = false)
    public double longitude;
    @Column(name = "latitude", nullable = false)
    public double latitude;
    @Column(name = "address")
    public String address;

}

