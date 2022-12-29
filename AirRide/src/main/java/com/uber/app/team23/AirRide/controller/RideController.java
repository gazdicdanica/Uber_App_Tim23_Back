package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController @RequestMapping("/api/ride")
public class RideController {

    @Autowired
    RideService rideService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideResponseDTO> createRide(@Valid @RequestBody RideDTO rideDTO){
        Ride ride = rideService.save(rideDTO);
        //check if user has already pending ride
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRideDriver(@PathVariable Long driverId){
        return null;
        //        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);
    }

    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRidePassenger(@PathVariable Long passengerId){
        return null;
        //        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRide(@PathVariable Long id){
        return null;
        //        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id){
        return null;
//        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);

    }

//    @PutMapping("/{id}/panic")
//    public ResponseEntity<PanicDTO> panic(@PathVariable Long id, @RequestBody Panic panic){
//        Panic p = new Panic();
//        p.setReason(panic.getReason());
//        p.setTime(LocalDateTime.now());
//        p.setId(id);
//        p.setUser(new Passenger((long)1, "Pera", "Peric", "111111", "+3811231234",
//                "test@gmail.com","sifra123", "Bulevar Oslobodjenja", false, true, null, null));
//        return new ResponseEntity<>(new PanicDTO(p), HttpStatus.OK);
//    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponseDTO> acceptRide(@PathVariable Long id){
        return null;
        //        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/end")
    public ResponseEntity<RideResponseDTO> endRide(@PathVariable Long id){
        return null;
        //        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);

    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id, @RequestBody Rejection rejection){
        return null;
//        return new ResponseEntity<>(rideService.getDTO(), HttpStatus.OK);

    }
}
