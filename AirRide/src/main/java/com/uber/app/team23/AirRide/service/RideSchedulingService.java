package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RideSchedulingService {
    @Autowired
    private  DriverService driverService;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private RideRepository rideRepository;

    private List<Driver> findAvailableDrivers(List<Driver> onlineDrivers){
        // Driver does not have pending nor active ride
        List<Driver> ret = new ArrayList<>();
        for(Driver onlineDriver : onlineDrivers){
            RideResponseDTO active = rideRepository.findActiveByDriver(onlineDriver.getId()).orElse(null);
            RideResponseDTO pending = rideRepository.findPendingByDriver(onlineDriver.getId()).orElse(null);

            if(active == null && pending == null){
                ret.add(onlineDriver);
            }
        }
        return ret;
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private Driver findClosestDriver(Ride ride, List<Driver> drivers){
        Location departure = new ArrayList<>(ride.getLocations()).get(0).getDeparture();
        Driver closest = drivers.get(0);
        double minDistance = 0;
        for(Driver d: drivers){
            Location current = d.getVehicle().getCurrentLocation();
            double distance = calculateDistance(departure.getLatitude(), departure.getLongitude(), current.getLatitude(), current.getLongitude());
            if(distance < minDistance){
                minDistance = distance;
                closest = d;
            }
        }
        return closest;
    }

    public Driver findDriver(Ride ride){
        List<Driver> onlineDrivers = driverService.findOnlineDrivers();
        List<Driver> driversWithAppropriateVehicle = onlineDrivers.stream().filter(driver -> driver.getVehicle().getVehicleType().getType() == ride.getVehicleType())
                .filter(driver -> driver.getVehicle().babyTransport == ride.isBabyTransport())
                .filter(driver -> driver.getVehicle().petTransport == ride.isPetTransport()).toList();

        if(onlineDrivers.isEmpty()){
            throw new BadRequestException("No drivers are online.");
        }if(driversWithAppropriateVehicle.isEmpty()){
            throw new BadRequestException("No driver is online with appropriate vehicle.");
        }
        // TODO obraditi slucaj - svi vozaci trenutno zauzeti i imaju zakazanu voznju

        List<Driver> availableDrivers = findAvailableDrivers(driversWithAppropriateVehicle);
        if(!availableDrivers.isEmpty()){
           return findClosestDriver(ride, availableDrivers);
        }

        return null;
    }
}
