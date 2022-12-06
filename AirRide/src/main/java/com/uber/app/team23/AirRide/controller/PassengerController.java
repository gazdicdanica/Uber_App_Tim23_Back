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

@RestController @RequestMapping("api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;


    @GetMapping(value ={"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassengerDTO> getPassenger(@PathVariable("id") Long id){
        Passenger p = passengerService.get(id);
//
//        if (p == null){
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }

        return new ResponseEntity<>(new PassengerDTO(p), HttpStatus.OK);

    }

    //TODO PAGING
    @GetMapping
    public ResponseEntity<List<Passenger>> getPassengersPage(Pageable page){
//        Page<Passenger> passengers = passengerService.getAll(page);
//        return new ResponseEntity<Passenger>(passengers, HttpStatus.OK);
        return null;
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<List<Ride>> getPassengerRidesPage(@PathVariable Long id, Pageable page, LocalDateTime from,
                                                            LocalDateTime to, String sortBy)
    {
        //TODO POGLEDATI VEZBE 7 - JPA-EXAMPLE
        return null;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody PassengerDTO passengerDTO){
        Passenger p = new Passenger();
        p.setId((long)1);
        p.setName(passengerDTO.getName());
        p.setLastName(passengerDTO.getLastName());
        p.setPassword(passengerDTO.getPassword());
        p.setAddress(passengerDTO.getAddress());
        p.setPhoneNumber(passengerDTO.getPhoneNumber());
        p.setProfilePhoto(passengerDTO.getProfilePhoto());
        p.setEmail(passengerDTO.getEmail());
//        p = passengerService.create(p);

        return new ResponseEntity<>(new PassengerDTO(p), HttpStatus.CREATED);
    }

    @PostMapping("/{activationId}")
    public ResponseEntity<Void> activatePassengerAccount(@PathVariable Long activationId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PassengerDTO> updatePassenger(@RequestBody PassengerDTO passengerDTO, @PathVariable Long id){
//        Passenger p = passengerService.get(id);
        passengerDTO.setId(1);
        Passenger p = new Passenger();

//        if (p == null){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        //TODO Actually update p
        p.setId((long)passengerDTO.getId());
        p.setName(passengerDTO.getName());
        p.setLastName(passengerDTO.getLastName());
        p.setPassword(passengerDTO.getPassword());
        p.setAddress(passengerDTO.getAddress());
        p.setPhoneNumber(passengerDTO.getPhoneNumber());
        p.setProfilePhoto(passengerDTO.getProfilePhoto());
        p.setEmail(passengerDTO.getEmail());

        return new ResponseEntity<>(new PassengerDTO(p), HttpStatus.OK);
    }



}
