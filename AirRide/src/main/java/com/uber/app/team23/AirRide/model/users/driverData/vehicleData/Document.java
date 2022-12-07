package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;

//import jakarta.persistence.*;

//@Entity @Table(name = "documents")
public class Document {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
//    @Column(name = "name")
    public String name;
//    @Column(name = "photo")
    public String photo;
//    @OneToOne

    public Driver driver;
}
