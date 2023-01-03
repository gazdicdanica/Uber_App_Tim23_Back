package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "vehicles")
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    public Driver driver;
    @Column(name = "vehicle_model")
    public String vehicleModel;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    public VehicleType vehicleType;
    @Column(name = "plates")
    public String licenseNumber;
    @Column(name = "capacity")
    public int capacity;
//        @Transient
//    public Location currentLocation;
    @Column(name = "babies")
    public boolean babyTransport;
    @Column(name = "pets")
    public boolean petTransport;

}
