package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.FavoriteDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<FavoriteDTO> getPassengerFavorites(User user){

        Passenger p = passengerService.findOne(user.getId());

        List<Favorite> favorites = favoriteRepository.findAll();
        List<FavoriteDTO> ret = new ArrayList<>();
        if(favorites.isEmpty()){
            return new ArrayList<>();
        }
        for(Favorite f : favorites){
            Passenger u = f.getPassengers().get(0);
            if (Objects.equals(u.getId(), p.getId())){
                ret.add(new FavoriteDTO(f));
            }
        }
        return ret;
    }

    public Favorite addPassengers(Long id, List<UserShortDTO> passengers){
        Favorite favorite = findOne(id);
        favorite.setPassengers(new ArrayList<>());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Passenger p = passengerService.findOne(user.getId());
        favorite.getPassengers().add(p);
        for(UserShortDTO pass : passengers){
            Passenger passenger = passengerService.findByEmail(pass.getEmail());
            favorite.getPassengers().add(passenger);
        }

        return favoriteRepository.save(favorite);
    }

    public Favorite addLocations(Long id, List<Route> routes){
        Favorite favorite = findOne(id);
        favorite.setLocations(new ArrayList<>());
        for(Route r: routes){
            Route route = routeService.findByLocationAddress(r.getDeparture().getAddress(), r.getDestination().getAddress());
            if(route == null){
                route = routeService.save(r);
            }
            favorite.getLocations().add(route);
        }
        return favoriteRepository.save(favorite);
    }

    public Favorite save(FavoriteDTO favorite){
        Favorite newFavorite = new Favorite();
        newFavorite.setFavoriteName(favorite.getFavoriteName());
        newFavorite.setPetTransport(favorite.isPetTransport());
        newFavorite.setVehicleType(favorite.getVehicleType());
        newFavorite.setBabyTransport(favorite.isBabyTransport());
        return this.favoriteRepository.save(newFavorite);
    }

    public void delete(Long id){
        Favorite fav = findOne(id);
        if (fav == null){
            throw new EntityNotFoundException("Favorite location does not exist!");
        }
        favoriteRepository.delete(fav);
    }
}
