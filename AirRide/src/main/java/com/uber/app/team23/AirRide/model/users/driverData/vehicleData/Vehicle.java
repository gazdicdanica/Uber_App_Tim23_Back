package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import jakarta.persistence.*;

import lombok.*;


@Entity
@Data
@NoArgsConstructor @AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    @JsonIgnore
    public Driver driver;

    @Column(name = "vehicle_model")
    public String vehicleModel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    public VehicleType vehicleType;

    @Column(name = "plates")
    public String licenseNumber;

    @Column(name = "capacity")
    public int passengerSeats;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_location")
    public Location currentLocation;

    @Column(name = "babies")
    public boolean babyTransport;

    @Column(name = "pets")
    public boolean petTransport;

}
