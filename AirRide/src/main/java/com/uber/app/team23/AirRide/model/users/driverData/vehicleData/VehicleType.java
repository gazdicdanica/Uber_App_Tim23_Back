package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@NoArgsConstructor
//@Table(name = "VehicleType")
public class VehicleType {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
//    @Column(name = "type")
    public VehicleEnum type;
//    @Column(name = "price")
    public double price;

    public VehicleType(Long id, VehicleEnum type, double price) {
        this.id = id;
        this.type = type;
        this.price = price;
    }
}
