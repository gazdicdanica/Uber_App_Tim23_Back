package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RouteService {
    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private LocationService locationService;

    public Route findById(Long id){
        return this.routeRepository.findById(id).orElse(null);
    }

    public Route findByLocationIds(Long departureId, Long destinationId){
        return routeRepository.findByLocationIds(departureId, destinationId).orElse(null);
    }

    public Route findByLocationAddress(String departureAddress, String destinationAddress){
        return routeRepository.findByLocationAddress(departureAddress, destinationAddress).orElse(null);
    }

    public Route save(Route route){

        Route r = new Route();
        System.err.println("ROUTE SERVICE DEPARTURE " + route.getDeparture().getAddress());
        Location departure = locationService.findByAddress(route.getDeparture().getAddress());
        if(departure == null){
            departure = route.getDeparture();
        }
        System.err.println("ROUTE SERVICE DESTINATION " + route.getDestination().getAddress());
        Location destination = locationService.findByAddress(route.getDestination().getAddress());
        if (destination == null){
            destination = route.getDestination();
        }

        r.setDeparture(departure);
        r.setDestination(destination);
        r.setDistance(route.getDistance());
        // TODO setDistance ???
//        Route existingRoute = this.findByLocationIds(departure.getId(), destination.getId());
//        if(existingRoute != null){
//            return existingRoute;
//        }

        return routeRepository.save(route);


    }
}
