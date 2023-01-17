package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @PutMapping("/{id}/location")
    public ResponseEntity<Void> changeLocation( @PathVariable Long id, @RequestBody Location location){

        // TODO Vehicle is not assigned to the specific driver! - 400

        vehicleService.changeLocation(id, location);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
