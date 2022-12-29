package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.RidePaginatedDTO;
import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.dto.UserPaginatedDTO;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.service.PassengerService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;

@RestController @RequestMapping("api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createPassenger(@Valid @RequestBody Passenger passenger) throws ConstraintViolationException {
        Passenger newPassenger = passengerService.save(passenger);

        return new ResponseEntity<>(new UserDTO(newPassenger), HttpStatus.OK);

    }

    // TODO
    @GetMapping(value ={"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getPassenger(@PathVariable("id") Long id){
        Passenger p = passengerService.findOne(id);
        return new ResponseEntity<>(new UserDTO(p), HttpStatus.OK);

    }

    // TODO
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPaginatedDTO> getPassengersPage(@RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(new UserPaginatedDTO(new ArrayList<>()), HttpStatus.OK);
    }

    // TODO
    @GetMapping("/{id}/ride")
    public ResponseEntity<RidePaginatedDTO> getPassengerRidesPage(@PathVariable Long id,  @RequestParam int page, @RequestParam int size,
                                                                  @RequestParam String sort, @RequestParam String from, @RequestParam String to)
    {
        return new ResponseEntity<>(new RidePaginatedDTO(new ArrayList<>()), HttpStatus.OK);
    }

    // TODO
    @GetMapping("/activate/{activationId}")
    public ResponseEntity<Void> activatePassengerAccount(@PathVariable Long activationId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // TODO
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updatePassenger(@RequestBody Passenger passenger, @PathVariable Long id){

        return new ResponseEntity<>(new UserDTO(passengerService.getMockPassenger()), HttpStatus.OK);
    }



}
