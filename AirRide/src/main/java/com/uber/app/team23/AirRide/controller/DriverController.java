package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DriverController {
    @PostMapping("/api/driver")
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver){
        return new ResponseEntity<>(driver, HttpStatus.OK);
    }
}
