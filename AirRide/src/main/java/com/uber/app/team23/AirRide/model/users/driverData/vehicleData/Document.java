package com.uber.app.team23.AirRide.model.users.driverData.vehicleData;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity @Table(name = "documents")
public class Document {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @Column(name = "name")
    public String name;
    @Column(name = "photo")
    public String photo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    public Driver driver;
}

