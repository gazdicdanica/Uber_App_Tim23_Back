package com.uber.app.team23.AirRide.model.rideData;

import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "favorites")
public class Favorite {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String favoriteName;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
    private List<Route> locations;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "favorites_passenger", joinColumns = @JoinColumn(name = "favorite_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "passenger_id", referencedColumnName = "id"))
    private List<Passenger> passengers;
    @Column(name = "vehicle_type")
    private VehicleEnum vehicleType;
    @Column(name = "baby_transport")
    private boolean babyTransport;
    @Column(name = "pet_transport")
    private boolean petTransport;
}
