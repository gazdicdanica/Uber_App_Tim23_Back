package com.uber.app.team23.AirRide.model.rideData;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "locations")
public class Location {
    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "longitude", nullable = false)
    public double longitude;
    @Column(name = "latitude", nullable = false)
    public double latitude;
    @Column(name = "address")
    public String address;

}

