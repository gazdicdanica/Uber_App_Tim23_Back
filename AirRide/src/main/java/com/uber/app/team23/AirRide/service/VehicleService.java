package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    LocationService locationService;

    public Vehicle findOne(Long id){
        return this.vehicleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vehicle does not exist"));
    }

    public void changeLocation(Long id, Location location){
        Vehicle vehicle = findOne(id);
        Location l = locationService.findByAddress(location.getAddress());
        if(l == null){
            l = locationService.save(location);
        }
        vehicle.setCurrentLocation(l);
        vehicleRepository.save(vehicle);
    }
}
