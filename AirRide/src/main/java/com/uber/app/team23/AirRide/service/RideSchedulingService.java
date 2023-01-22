package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

@Service
public class RideSchedulingService {
    @Autowired
    private  DriverService driverService;
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private WorkingHoursService workingHoursService;


    public boolean areAllDriversOccupied(List<Driver> drivers, LocalDateTime scheduledTime){
        for(Driver d: drivers){
            RideResponseDTO active = rideRepository.findActiveByDriver(d.getId()).orElse(null);
            RideResponseDTO accepted = rideRepository.findAcceptedByDriver(d.getId()).orElse(null);
            if(active == null || accepted== null){
                return false;
            }
        }return true;
    }

    public List<Double> getEstimates(Location location1, Location location2){
        String uri = this.createUri(location1, location2);
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        JSONObject obj = new JSONObject(result);
        JSONArray routes = obj.getJSONArray("routes");
        JSONObject o = (JSONObject) routes.get(0);
        double distanceInM = o.getDouble("distance");
        double durationInSec = o.getDouble("duration");

        List<Double> ret = new ArrayList<>();
        ret.add(durationInSec);
        ret.add(distanceInM/1000);
        return ret;
    }

    private String createUri(Location location1, Location location2){
        StringBuilder uri = new StringBuilder("https://router.project-osrm.org/route/v1/driving/");
        uri.append(location1.getLongitude());
        uri.append(",");
        uri.append(location1.getLatitude());
        uri.append(";");
        uri.append(location2.getLongitude());
        uri.append(",");
        uri.append(location2.getLatitude());
        uri.append("?overview=false&geometries=geojson");
        return uri.toString();
    }

    private Driver findFastestDriver(List<Driver> drivers, Location departure){
        int leastTimeNeeded = 100000;
        Driver ret = null;
        for(Driver driver : drivers){
            RideResponseDTO accepted = rideRepository.findAcceptedByDriver(driver.getId()).orElse(null);
            if(accepted != null){
                continue;
            }
            RideResponseDTO active = rideRepository.findActiveByDriver(driver.getId()).orElse(null);
            Driver d = driverService.findById(driver.getId());
            Location current = d.getVehicle().getCurrentLocation();
            if(active == null){
                int time = (int) Math.round(this.getEstimates(current, departure).get(0));
                if(time < leastTimeNeeded){
                    leastTimeNeeded = time;
                    ret = d;
                }
            }else{
                int finishTime = active.getEstimatedTimeInMinutes();
                int time = (int) Math.round(this.getEstimates(active.getLocations().get(active.getLocations().size()-1).getDestination(), departure).get(0)/60);
                if((finishTime + time) < leastTimeNeeded){
                    leastTimeNeeded = finishTime + time;
                    ret = d;
                }

            }
        }

        return ret;

    }

    public Driver findDriver(Ride ride){
        List<Driver> onlineDrivers = driverService.findOnlineDrivers();
        if(onlineDrivers.isEmpty()){
            throw new BadRequestException("No drivers are online.");
        }
        List<Driver> driversWithAppropriateVehicle = onlineDrivers.stream().filter(driver -> driver.getVehicle()!=null && driver.getVehicle().getVehicleType().getType() == ride.getVehicleType())
                .filter(driver -> driver.getVehicle().babyTransport == ride.isBabyTransport() || driver.getVehicle().babyTransport)
                .filter(driver -> driver.getVehicle().petTransport == ride.isPetTransport() || driver.getVehicle().petTransport).toList();

        List<Driver> driversWorkHours = driversWithAppropriateVehicle.stream().filter(driver -> workingHoursService.calculateWorkingHours(driver) < 8).toList();
        if(driversWithAppropriateVehicle.isEmpty()){
            throw new BadRequestException("No driver is online with appropriate vehicle.");
        }if(areAllDriversOccupied(onlineDrivers, ride.getScheduledTime()) || driversWorkHours.isEmpty()){
            // no drivers are available and all have scheduled rides
            throw new BadRequestException("No driver is available at the moment.");
        }

        List<Route> routes = new ArrayList<>(ride.getLocations());
        return findFastestDriver(driversWorkHours, routes.get(0).getDeparture());

    }
}
