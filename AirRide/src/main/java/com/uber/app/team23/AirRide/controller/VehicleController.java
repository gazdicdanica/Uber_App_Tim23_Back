package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.UpdateLocationDTO;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.service.LocationService;
import com.uber.app.team23.AirRide.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/api/vehicle")
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @Autowired
    LocationService locationService;

    @PutMapping("/{id}/location")
//    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @Transactional
    public ResponseEntity<Void> changeLocation(@PathVariable Long id, @Valid @RequestBody Location location){
        vehicleService.changeLocation(id, location);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping
    @Transactional
    public ResponseEntity<Vehicle> updateLocation(@RequestBody UpdateLocationDTO dto) {
        Vehicle vehicle = vehicleService.findOneByLicensePlate(dto.getLicenseNumber());
        if (vehicle == null) {
            throw new EntityNotFoundException("Vehicle Does Not Exist");
        }
        vehicle.getCurrentLocation().setLatitude(dto.getLatitude());
        vehicle.getCurrentLocation().setLongitude(dto.getLongitude());
        System.err.println(vehicle);
        vehicleService.save(vehicle);
        return new ResponseEntity<>(vehicle, HttpStatus.OK);
    }



    @GetMapping("/vehicleTypes")
    public ResponseEntity<List<VehicleType>> findAllVehicleType(){
        return new ResponseEntity<>(vehicleService.findAllVehicleTypes(), HttpStatus.OK);
    }
}
