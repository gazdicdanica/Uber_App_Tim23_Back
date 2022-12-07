package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
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
        v.setVehicleType(new VehicleType((long)1, VehicleEnum.STANDARD, 123));
        r.setVehicle(v);
        ArrayList<Route> locations = new ArrayList<>();
        locations.add(new Route());

        return new RideResponseDTO(r, locations, passengers);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideResponseDTO> createRide(@RequestBody RideDTO rideDTO){

        return new ResponseEntity<>(getDTO(), HttpStatus.CREATED);
    }

    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRideDriver(@PathVariable Long driverId){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);
    }

    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRidePassenger(@PathVariable Long passengerId){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRide(@PathVariable Long id){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/panic")
    public ResponseEntity<PanicDTO> panic(@PathVariable Long id, @RequestBody Panic panic){
        Panic p = new Panic();
        p.setReason(panic.getReason());
        p.setTime(LocalDateTime.now());
        p.setId(id);
        p.setUser(new Passenger((long)1, "Pera", "Peric", "111111", "+3811231234",
                "test@gmail.com","sifra123", "Bulevar Oslobodjenja", false, true, null, null));
        return new ResponseEntity<>(new PanicDTO(p), HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponseDTO> acceptRide(@PathVariable Long id){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/end")
    public ResponseEntity<RideResponseDTO> endRide(@PathVariable Long id){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id, @RequestBody Rejection rejection){
        return new ResponseEntity<>(getDTO(), HttpStatus.OK);

    }
}
