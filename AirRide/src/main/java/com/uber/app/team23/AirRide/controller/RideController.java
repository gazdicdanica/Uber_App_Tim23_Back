package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.service.PanicService;
import com.uber.app.team23.AirRide.service.RideService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController @RequestMapping("/api/ride")
public class RideController {

    @Autowired
    RideService rideService;

    @Autowired
    PanicService panicService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideResponseDTO> createRide(@Valid @RequestBody RideDTO rideDTO){
        Ride ride = rideService.save(rideDTO);
        ride = rideService.addPassengers(rideDTO, ride.getId());
        ride = rideService.addRoutes(rideDTO, ride.getId());
        // TODO check if user has already pending ride (response status 400)
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);
    }

    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRideDriver(@PathVariable Long driverId){
        RideResponseDTO ride = rideService.findActiveByDriver(driverId);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRidePassenger(@PathVariable Long passengerId){
        RideResponseDTO ride = rideService.findActiveByPassenger(passengerId);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRide(@PathVariable Long id){
        // TODO response status 400 - bad id format???
        Ride ride = rideService.findOne(id);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);

    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.withdrawRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @Transactional
    @PutMapping("/{id}/panic")
    public ResponseEntity<PanicDTO> panic(@PathVariable Long id, @RequestBody Panic panic){
        Ride ride = rideService.setPanic(id, panic);
        PanicDTO p = panicService.save(panic, ride);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<RideResponseDTO> startRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.startRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponseDTO> acceptRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.acceptRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PutMapping("/{id}/end")
    public ResponseEntity<RideResponseDTO> endRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.endRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id, @RequestBody Rejection rejection){
        RideResponseDTO ride = rideService.cancelRide(id, rejection);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

//    @PostMapping("/favorites")
//    public ResponseEntity<> setFavorite(@RequestBody )
}
