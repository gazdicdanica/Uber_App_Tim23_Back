package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.PassengerRideDTO;
import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController @RequestMapping("/api/ride")
public class RideController {

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideResponseDTO> createRide(@RequestBody RideDTO rideDTO){
        Ride ride = new Ride();
        ride.setStart(LocalDateTime.now());
        ride.setStart(LocalDateTime.now().plusMinutes(10));
        ride.setTotalPrice(1235);
        Driver d = new Driver();
        d.setId((long)1);
        d.setEmail("test@email.com");
        ride.setDriver(d);
        ride.setTimeEstimate(10);
        ride.setRideStatus(RideStatus.PENDING);
        ride.setBabies(rideDTO.isBabyTransport());
        ride.setPets(rideDTO.isPetTransport());
        Vehicle v = new Vehicle();
        v.setVehicleType(new VehicleType((long)1, VehicleEnum.STANDARD, 123));
        ride.setVehicle(v);

        return new ResponseEntity<>(new RideResponseDTO(ride, rideDTO.getLocations(), rideDTO.getPassengers()), HttpStatus.CREATED);
    }

    @GetMapping("/active/{driverId}")
    public ResponseEntity<RideResponseDTO> getActiveRide(@PathVariable Long driverId){
        Ride r = new Ride((long)1, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), 1234, null, 10, null, null,
                RideStatus.ACTIVE, null, false, true, true, null, null);
        Driver d = new Driver();
        d.setId(driverId);
        d.setEmail("test@gmail.com");
        r.setDriver(d);
        ArrayList<PassengerRideDTO> passengers= new ArrayList<>();
        passengers.add(new PassengerRideDTO(1, "email"));
        passengers.add(new PassengerRideDTO(2, "email"));
        Vehicle v = new Vehicle();
        v.setVehicleType(new VehicleType((long)1, VehicleEnum.STANDARD, 123));
        r.setVehicle(v);
        ArrayList<Location> locations = new ArrayList<>();
        locations.add(new Location((long)1, 12.33, 12.34, "Bulevar Oslobodjenja 45"));
        locations.add(new Location());

        return new ResponseEntity<>(new RideResponseDTO(r, locations, passengers), HttpStatus.OK);
    }
}
