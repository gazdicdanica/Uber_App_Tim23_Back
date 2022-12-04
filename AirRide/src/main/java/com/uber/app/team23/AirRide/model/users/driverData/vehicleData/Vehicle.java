package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter @NoArgsConstructor
@Table(name = "Vehicle")
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Driver driver;
    @Column(name = "vehicleModel")
    public String VehicleModel;
    @OneToOne
    public VehicleType vehicle;
    @Column(name = "plates")
    public String plates;
    @Column(name = "capacity")
    public int capacity;
    @Transient
    public Location currentLocation;
    @Column(name = "babies")
    public boolean acceptBabies;
    @Column(name = "pets")
    public boolean acceptPets;

    public Vehicle(Long id, Driver driver, String vehicleModel, VehicleType vehicle, String plates, int capacity,
                   Location currentLocation, boolean acceptBabies, boolean acceptPets) {
        this.id = id;
        this.driver = driver;
        VehicleModel = vehicleModel;
        this.vehicle = vehicle;
        this.plates = plates;
        this.capacity = capacity;
        this.currentLocation = currentLocation;
        this.acceptBabies = acceptBabies;
        this.acceptPets = acceptPets;
    }
}
