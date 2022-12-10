package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class RideService {

    public RideResponseDTO getDTO(){
        Ride r = new Ride((long)1, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), 1234, null, 10, null, null,
                RideStatus.ACTIVE, null, false, true, true, null, null);
        Driver d = new Driver();
        d.setId((long)1);
        d.setEmail("test@gmail.com");
        r.setDriver(d);
        ArrayList<UserShortDTO> passengers= new ArrayList<>();
        passengers.add(new UserShortDTO(1, "email"));
        passengers.add(new UserShortDTO(2, "email"));
        Vehicle v = new Vehicle();
        v.setVehicleType(new VehicleType((long)1, VehicleEnum.STANDARDNO, 123));
        r.setVehicle(v);
        ArrayList<Route> locations = new ArrayList<>();
        locations.add(new Route());

        return new RideResponseDTO(r, locations, passengers);
    }
}
