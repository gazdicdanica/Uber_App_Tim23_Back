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
            for(Passenger u: f.getPassengers()){
                if (Objects.equals(u.getId(), p.getId())){
                    ret.add(new FavoriteDTO(f));
                }
            }
        }
        return ret;
    }

    public Favorite addPassengers(Long id, Set<UserShortDTO> passengers){
        Favorite favorite = findOne(id);
        favorite.setPassengers(new HashSet<>());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        for(UserShortDTO p : passengers){
            Passenger passenger = passengerService.findOne((long)p.getId());
            favorite.getPassengers().add(passenger);
        }
        Passenger p = passengerService.findOne(user.getId());
        favorite.getPassengers().add(p);
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
        // TODO number of favorites cannot exceed 10

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
