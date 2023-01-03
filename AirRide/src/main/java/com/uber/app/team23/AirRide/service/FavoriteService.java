package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.FavoriteDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FavoriteService {
    @Autowired
    FavoriteRepository favoriteRepository;
    @Autowired
    PassengerService passengerService;
    @Autowired
    RouteService routeService;

    public Favorite findOne(Long id){
        return this.favoriteRepository.findById(id).orElse(null);
    }

    public Favorite addPassengers(Long id, Set<UserShortDTO> passengers){
        Favorite favorite = findOne(id);
        favorite.setPassengers(new HashSet<>());
        for(UserShortDTO p : passengers){
            Passenger passenger = passengerService.findOne((long)p.getId());
            favorite.getPassengers().add(passenger);
        }
        return favoriteRepository.save(favorite);
    }

    public Favorite addLocations(Long id, Set<Route> routes){
        Favorite favorite = findOne(id);
        favorite.setLocations(new HashSet<>());
        for(Route r: routes){
            Route route = routeService.findByLocationAddress(r.getDeparture().getAddress(), r.getDestination().getAddress());
            if(route == null){
                route = routeService.save(r);
            }
            favorite.getLocations().add(route);
        }
        return favoriteRepository.save(favorite);
    }

//    @Transactional
    public Favorite save(FavoriteDTO favorite){
        Favorite newFavorite = new Favorite();
        newFavorite.setFavoriteName(favorite.getFavoriteName());
        newFavorite.setPetTransport(favorite.isPetTransport());
        newFavorite.setVehicleType(favorite.getVehicleType());
        newFavorite.setBabyTransport(favorite.isBabyTransport());
        return this.favoriteRepository.save(newFavorite);
    }
}
