package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.mapper.FavoriteDTOMapper;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.service.FavoriteService;
import com.uber.app.team23.AirRide.service.PanicService;
import com.uber.app.team23.AirRide.service.RideService;
import com.uber.app.team23.AirRide.service.UserService;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/api/ride", produces = MediaType.APPLICATION_JSON_VALUE)
public class RideController {

    @Autowired
    RideService rideService;
    @Autowired
    PanicService panicService;
    @Autowired
    FavoriteService favoriteService;
    @Autowired
    UserService userService;

    @Autowired
    WebSocketController webSocketController;

    @Transactional
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<RideResponseDTO> createRide(@Valid @RequestBody @Nullable RideDTO rideDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Ride ride = rideService.save(rideDTO);
        ride = rideService.addRoutes(rideDTO, ride.getId());
        ride = rideService.addPassengers(rideDTO, ride.getId(), user.getId());
        Driver potential = rideService.findPotentialDriver(ride);
        ride = rideService.addDriver(ride, potential);
        RideResponseDTO dto = new RideResponseDTO(ride);
        webSocketController.simpMessagingTemplate.convertAndSend("/ride", dto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRideDriver(@PathVariable Long driverId){
        RideResponseDTO ride = rideService.findActiveByDriver(driverId);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRidePassenger(@PathVariable Long passengerId){
        RideResponseDTO ride = rideService.findActiveByPassenger(passengerId);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'ROLE_DRIVER')")
    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDTO> getRide(@PathVariable Long id){
        if(id == null){
            throw new BadRequestException("Bad id format");
        }
        Ride ride = rideService.findOne(id);
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<RideResponseDTO> withdrawRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.withdrawRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_USER')")
    @Transactional
    @PutMapping("/{id}/panic")
    public ResponseEntity<PanicDTO> panic(@PathVariable Long id, @Nullable @RequestBody Panic panic){
        Ride ride = rideService.setPanic(id);
        if(panic != null){
            PanicDTO p = panicService.save(panic, ride);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }
        return new ResponseEntity<>(new PanicDTO(), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/start")
    public ResponseEntity<RideResponseDTO> startRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.startRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponseDTO> acceptRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.acceptRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/end")
    public ResponseEntity<RideResponseDTO> endRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.endRide(id);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id, @Nullable @RequestBody Rejection rejection){
        RideResponseDTO ride = rideService.cancelRide(id, rejection);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Transactional
    @PostMapping("/favorites")
    public ResponseEntity<FavoriteDTO> setFavorite(@Valid @Nullable @RequestBody FavoriteDTO favorite){
        if(favorite == null){
            return new ResponseEntity<>(new FavoriteDTO(), HttpStatus.OK);
        }
        Favorite newFavorite = favoriteService.save(favorite);
        newFavorite = favoriteService.addLocations(newFavorite.getId(), favorite.getLocations());
        newFavorite = favoriteService.addPassengers(newFavorite.getId(), favorite.getPassengers());
        return new ResponseEntity<>(FavoriteDTOMapper.fromFavoriteToDTO(newFavorite), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteDTO>> getFavorites(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FavoriteDTO> favs = favoriteService.getPassengerFavorites(user);
        return new ResponseEntity<>(favs, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('USER_ROLE')")
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id){
        favoriteService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
