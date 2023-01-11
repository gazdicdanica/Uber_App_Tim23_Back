package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

import java.util.ArrayList;
import java.util.List;

@Service
public class RideSchedulingService {
    @Autowired
    private  DriverService driverService;

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private WorkingHoursService workingHoursService;


    private List<Driver> findAvailableDrivers(List<Driver> onlineDrivers){
        // Driver does not have accepted nor active ride
        List<Driver> ret = new ArrayList<>();
        for(Driver onlineDriver : onlineDrivers){
            RideResponseDTO active = rideRepository.findActiveByDriver(onlineDriver.getId()).orElse(null);

            if(active == null){
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

    public boolean areAllDriversOccupied(List<Driver> drivers){
        for(Driver d: drivers){
            RideResponseDTO active = rideRepository.findActiveByDriver(d.getId()).orElse(null);
            RideResponseDTO accepted = rideRepository.findAcceptedByDriver(d.getId()).orElse(null);
            if(active == null || accepted == null){
                return false;
            }
        }return true;
    }

    public Driver findClosestDriversWithNoScheduledRide(List<Driver> drivers){
        Driver ret = drivers.get(0);
        int minutes = 0;
        for(Driver d: drivers){
            RideResponseDTO active = rideRepository.findActiveByDriver(d.getId()).orElse(null);
            RideResponseDTO pending = rideRepository.findAcceptedByDriver(d.getId()).orElse(null);
            if(active != null && pending == null){
                if(active.getEstimatedTimeInMinutes() < minutes){
                    minutes = active.getEstimatedTimeInMinutes();
                    ret = d;
                }
            }
        }
        return ret;
    }


    public int calculateWorkingHours(Long id){
        Driver driver = driverService.findById(id);
        int hours = 0;
        List<WorkingHours> workingHours = workingHoursService.findByDriverInLastDay(driver);
        for(WorkingHours wh : workingHours){
            if(wh.getEnd() != null){
                hours += Math.abs(Duration.between(wh.getEnd(), wh.getStart()).toHours());
            }
        }
        System.err.println(id + " hours: " + hours);
        return hours;
    }

    public Driver findDriver(Ride ride){
        List<Driver> onlineDrivers = driverService.findOnlineDrivers();
        List<Driver> driversWithAppropriateVehicle = onlineDrivers.stream().filter(driver -> driver.getVehicle().getVehicleType().getType() == ride.getVehicleType())
                .filter(driver -> driver.getVehicle().babyTransport == ride.isBabyTransport())
                .filter(driver -> driver.getVehicle().petTransport == ride.isPetTransport()).toList();

        List<Driver> driversWorkHours = driversWithAppropriateVehicle.stream().filter(driver -> calculateWorkingHours(driver.getId()) < 8).toList();
        if(onlineDrivers.isEmpty()){
            throw new BadRequestException("No drivers are online.");
        }if(driversWithAppropriateVehicle.isEmpty()){
            throw new BadRequestException("No driver is online with appropriate vehicle.");
        }if(areAllDriversOccupied(onlineDrivers)){
            // no drivers are available and all have scheduled rides
            throw new BadRequestException("No driver is available at the moment.");
        }

        List<Driver> availableDrivers = findAvailableDrivers(driversWorkHours);

        if(!availableDrivers.isEmpty()){
            //TODO radno vreme > 8h
           return findClosestDriver(ride, availableDrivers);
        }
        //not available with no scheduled ride
        return findClosestDriversWithNoScheduledRide(driversWorkHours);
    }
}
