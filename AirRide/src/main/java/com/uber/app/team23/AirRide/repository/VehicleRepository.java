package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    @Query("select v from Vehicle v where v.driver.id=?1")
    public Vehicle findAllByDriver(Long id);


    @Query("select v from Vehicle v join VehicleType vt where v.vehicleType = vt and vt.type = ?1 and v.babyTransport = ?2 and v.petTransport = ?3")
    List<Vehicle> findVehicleByRideParameters(VehicleEnum vehicleEnum, boolean babyTransport, boolean petTransport);

}
