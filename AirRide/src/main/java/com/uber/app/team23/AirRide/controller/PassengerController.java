package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.PassengerDTO;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController @RequestMapping("/putnik")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;


    @GetMapping(value ={"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Passenger> getPassenger(@PathVariable("id") Long id){
        Passenger p = passengerService.get(id);

        if (p == null){
            return new ResponseEntity<Passenger>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Passenger>(p, HttpStatus.OK);

    }

    //TODO PAGING
    @GetMapping
    public ResponseEntity<List<Passenger>> getPassengersPage(Pageable page){
//        Page<Passenger> passengers = findAll(page);
//        return new ResponseEntity<Passenger>(passengers, HttpStatus.OK);
        return null;
    }

    @GetMapping("/{id}/voznja")
    public ResponseEntity<List<Ride>> getPassengerRidesPage(@PathVariable Long id, Pageable page, LocalDateTime from,
                                                            LocalDateTime to, String sortBy)
    {
        //TODO POGLEDATI VEZBE 7 - JPA-EXAMPLE
        return null;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Passenger> createPassenger(){
        Passenger p = new Passenger();
        Passenger createdPassenger = passengerService.create(p);
        return new ResponseEntity<>(createdPassenger, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Passenger> updatePassenger(@RequestBody Passenger passenger, @PathVariable Long id){
        Passenger p = passengerService.get(id);

        if (p == null){
            return new ResponseEntity<Passenger>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //TODO Actually update p

        return new ResponseEntity<Passenger>(p, HttpStatus.OK);
    }



}
