package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.FavoriteDTOMapper;
import com.uber.app.team23.AirRide.mapper.RideDTOMapper;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.service.*;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    RideSchedulingService rideSchedulingService;

    @Autowired
    WebSocketController webSocketController;

//    @Scheduled(fixedRate = 1000 * 2)
//    @Transactional
//    public void simulate() {
//        rideService.updateLocations(RideStatus.ACCEPTED);
//        rideService.updateLocations(RideStatus.ACTIVE);
//    }

//    @Scheduled(fixedRate = 2000)
//    @Transactional
//    public void sendOnLocationNotification(){
//        for(Ride r : rideService.findByStatus(RideStatus.ACCEPTED)){
//            Location currentLocation = r.getVehicle().getCurrentLocation();
//            Location departure = r.getLocations().get(0).getDeparture();
//            List<Double> estimates =  rideSchedulingService.getEstimates(currentLocation, departure);
//            Double distance = estimates.get(1);
//            if(distance < 0.1){
//                System.err.println("DRIVER STIGAO");
//                System.err.println("RIDE ID " + r.getId());
//                webSocketController.simpMessagingTemplate.convertAndSend("/driver-arrived/"+r.getId(), "");
//            }
//        }
//    }


    @Transactional
    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> createRide(@Valid @RequestBody @Nullable RideDTO rideDTO){

        if (rideDTO.getScheduledTime() == null) {
            System.err.println("scheduled time null");
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(rideService.findActiveByPassenger(user.getId()) != null){
                throw new BadRequestException("Cannot order a ride while you have an active one");
            }
            rideService.checkPassengerRide(user.getId());
            Ride ride = rideService.save(rideDTO);
            ride = rideService.addRoutes(rideDTO, ride.getId());
            ride = rideService.addPassengers(rideDTO, ride.getId(), user.getId());
            Driver potential = rideService.findPotentialDriver(ride);
            if(potential == null){
                throw new BadRequestException("No driver is available at the moment");
            }
            ride = rideService.addDriver(ride, potential);
            RideResponseDTO dto = new RideResponseDTO(ride);
            webSocketController.simpMessagingTemplate.convertAndSend("/ride-driver/" + potential.getId(), dto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            System.err.println("not null");
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Ride ride = rideService.save(rideDTO);
            ride = rideService.addRoutes(rideDTO, ride.getId());
            rideService.addPassengers(rideDTO, ride.getId(), user.getId());
            JSONObject resp = new JSONObject();
            return new ResponseEntity<>(resp.put("response", "Ride Notified").toString(), HttpStatus.OK);
        }
    }

    @Transactional
    @GetMapping("/active")
    public ResponseEntity<List<RideResponseDTO>> getActive(){
        List<Ride> actives = rideService.findByStatus(RideStatus.ACTIVE);
        List<RideResponseDTO> ret = actives.stream().map(RideDTOMapper::fromRideToDTO).toList();
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DRIVER')")
    @GetMapping("/driver/{driverId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRideDriver(@PathVariable Long driverId){
        RideResponseDTO ride = rideService.findActiveByDriver(driverId);
        if(ride == null){
            throw new EntityNotFoundException("Active ride for this driver does not exist");
        }
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/passenger/{passengerId}/active")
    public ResponseEntity<RideResponseDTO> getActiveRidePassenger(@PathVariable Long passengerId){
        System.err.println("Passenger ID: " + passengerId);
        RideResponseDTO ride = rideService.findActiveByPassenger(passengerId);
        if(ride == null){
            throw new EntityNotFoundException("Active ride for this passenger does not exist");
        }
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
        System.err.println(ride.getLocations());
        return new ResponseEntity<>(new RideResponseDTO(ride), HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PutMapping("/{id}/withdraw")
    public ResponseEntity<RideResponseDTO> withdrawRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.withdrawRide(id);
        if(ride.getDriver() != null){
            webSocketController.simpMessagingTemplate.convertAndSend("/ride-cancel/" + ride.getDriver().getId(), ride);
        }

        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_USER')")
    @Transactional
    @PutMapping("/{id}/panic")
    public ResponseEntity<PanicDTO> panic(@PathVariable Long id, @Nullable @RequestBody Panic panic){
        Ride ride = rideService.setPanic(id);
        if(panic != null){
            PanicDTO p = panicService.save(panic, ride);
            for(Passenger passenger : ride.getPassengers()){
                   webSocketController.simpMessagingTemplate.convertAndSend("/ride-panic/"+passenger.getId(), new RideResponseDTO(ride));
            }
            webSocketController.simpMessagingTemplate.convertAndSend("/ride-panic/"+ride.getDriver().getId(), new RideResponseDTO(ride));

            return new ResponseEntity<>(p, HttpStatus.OK);
        }
        return new ResponseEntity<>(new PanicDTO(panic.getUser(), panic.getReason()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/start")
    public ResponseEntity<RideResponseDTO> startRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.startRide(id);
        for(UserShortDTO p : ride.getPassengers()){
            webSocketController.simpMessagingTemplate.convertAndSend("/ride-passenger/"+p.getId(), ride);
        }
        return new ResponseEntity<>(ride, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/accept")
    public ResponseEntity<RideResponseDTO> acceptRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.acceptRide(id);
        int i =0;
        for(UserShortDTO user : ride.getPassengers()){
            if(i == 0){
                if (ride.getScheduledTime() != null) {
                    webSocketController.simpMessagingTemplate.convertAndSend("/scheduledNotifications/" + user.getId(), ride);
                }
                webSocketController.simpMessagingTemplate.convertAndSend("/ride-passenger/" + user.getId(), ride);
                i++;
                continue;
            }
            webSocketController.simpMessagingTemplate.convertAndSend("/linkPassengers/" + user.getId(), ride);
            i++;
        }
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/end")
    public ResponseEntity<RideResponseDTO> endRide(@PathVariable Long id){
        RideResponseDTO ride = rideService.endRide(id);
        System.err.println("PUTNICI: " + ride.getPassengers());
        for(UserShortDTO p : ride.getPassengers()){
            webSocketController.simpMessagingTemplate.convertAndSend("/ride-passenger/"+p.getId(), ride);
        }
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDTO> cancelRide(@PathVariable Long id, @RequestBody Rejection rejection){
        RideResponseDTO ride = rideService.cancelRide(id, rejection);
        webSocketController.simpMessagingTemplate.convertAndSend("/ride-passenger/"+ride.getPassengers().get(0).getId(), ride);
        return new ResponseEntity<>(ride, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @Transactional
    @PostMapping("/favorites")
    public ResponseEntity<FavoriteDTO> setFavorite(@Valid @RequestBody FavoriteDTO favorite){
        System.err.println("USAOOOO");
        Favorite newFavorite = favoriteService.save(favorite);
        newFavorite = favoriteService.addLocations(newFavorite.getId(), favorite.getLocations());
        newFavorite = favoriteService.addPassengers(newFavorite.getId(), favorite.getPassengers());
        System.err.println("Test na beack=u" +FavoriteDTOMapper.fromFavoriteToDTO(newFavorite));
        return new ResponseEntity<>(FavoriteDTOMapper.fromFavoriteToDTO(newFavorite), HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteDTO>> getFavorites(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<FavoriteDTO> favs = favoriteService.getPassengerFavorites(user);
        System.err.println(favs);
        return new ResponseEntity<>(favs, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('USER_ROLE')")
    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id){
        favoriteService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @Scheduled(fixedRate = 1000 * 60 * 1)
//    public void scheduledRides() {
//        List<Ride> rides = rideService.findAll();
//        rides = rideService.filterRidesForScheduling(rides);
//        for (Ride ride : rides) {
//            try{
//                Driver driver = rideService.findPotentialDriver(ride);
//                ride = rideService.addDriver(ride, driver);
//                RideResponseDTO dto = new RideResponseDTO(ride);
//                webSocketController.simpMessagingTemplate.convertAndSend("/ride-driver/" + driver.getId(), dto);
//            }catch(BadRequestException ex){
//                System.err.println("Bad request");
//                rideService.withdrawRide(ride.getId());
//                for(Passenger p : ride.getPassengers()){
//                    System.err.println( "passenger " + p.getId());
//                    System.err.println(ex.getMessage());
//                    RideResponseDTO dto = new RideResponseDTO(ride);
//                    webSocketController.simpMessagingTemplate.convertAndSend("/scheduledNotifications/"+ p.getId(), dto);
//                }
//            }
//
//
//        }
//    }
//    @Scheduled(fixedRate = 1337 * 1)
//    public void notification15Minutes() {
//        List<Ride> rides = rideService.findByStatus(RideStatus.PENDING);
//        rides = rideService.filterRidesForNotification(rides, 15);
//        for (Ride ride : rides) {
//            for (Passenger p : ride.getPassengers()) {
//                webSocketController.simpMessagingTemplate.convertAndSend("/notify15/" + p.getId(), "");
//            }
//        }
//    }
//
//    @Scheduled(fixedRate = 1337 * 1)
//    public void notification10Minutes() {
//        List<Ride> rides = rideService.findByStatus(RideStatus.PENDING);
//        rides = rideService.filterRidesForNotification(rides, 10);
//        for (Ride ride : rides) {
//            for (Passenger p : ride.getPassengers()) {
//                webSocketController.simpMessagingTemplate.convertAndSend("/notify10/" + p.getId(), "");
//            }
//        }
//    }
//    @Scheduled(fixedRate = 1337 * 1)
//    public void notification5Minutes() {
//        List<Ride> rides = rideService.findByStatus(RideStatus.PENDING);
//        rides = rideService.filterRidesForNotification(rides, 5);
//        for (Ride ride : rides) {
//            for (Passenger p : ride.getPassengers()) {
//                webSocketController.simpMessagingTemplate.convertAndSend("/notify5/" + p.getId(), "");
//            }
//        }
//    }

}
