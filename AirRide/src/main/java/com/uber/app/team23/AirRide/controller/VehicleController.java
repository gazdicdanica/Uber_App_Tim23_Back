package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.model.rideData.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api/vehicle")
public class VehicleController {

    @PutMapping("/{id}/location")
    public ResponseEntity<Void> changeLocation(@RequestBody Location location){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
