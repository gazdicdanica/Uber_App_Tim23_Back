package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location findByAddress(String address){
        return locationRepository.findByAddress(address).orElse(null);
    }

    public Location findById(Long id){
        return locationRepository.findById(id).orElse(null);
    }

    public Location save(Location location){
        Location existingLocation = this.findByAddress(location.address);
        if(existingLocation != null){
            // ride already persisted
            return existingLocation;
        }

        return locationRepository.save(location);
    }
}
