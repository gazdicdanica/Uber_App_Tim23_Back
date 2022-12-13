package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "vehicle_type")
public class VehicleType {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "type")
    public VehicleEnum type;
    @Column(name = "price")
    public double price;

}
