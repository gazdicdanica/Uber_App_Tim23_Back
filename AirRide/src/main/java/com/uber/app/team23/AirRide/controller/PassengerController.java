package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.PassengerDTO;
import com.uber.app.team23.AirRide.dto.PassengerPaginatedDTO;
import com.uber.app.team23.AirRide.dto.RidePaginatedDTO;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController @RequestMapping("api/passenger")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;


    @GetMapping(value ={"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getPassenger(@PathVariable("id") Long id){
        return new ResponseEntity<>(new UserDTO(passengerService.getMockPassenger()), HttpStatus.OK);

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserPaginatedDTO> getPassengersPage(@RequestParam int page, @RequestParam int size){
        return new ResponseEntity<>(new UserPaginatedDTO(new ArrayList<>()), HttpStatus.OK);
    }

    @GetMapping("/{id}/ride")
    public ResponseEntity<RidePaginatedDTO> getPassengerRidesPage(@PathVariable Long id,  @RequestParam int page, @RequestParam int size,
                                                                  @RequestParam String sort, @RequestParam String from, @RequestParam String to)
    {
        return new ResponseEntity<>(new RidePaginatedDTO(new ArrayList<>()), HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<PassengerDTO> createPassenger(@RequestBody Passenger passenger){
        return new ResponseEntity<>(new PassengerDTO(passengerService.getMockPassenger()), HttpStatus.OK);

    }

    @GetMapping("/activate/{activationId}")
    public ResponseEntity<Void> activatePassengerAccount(@PathVariable Long activationId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updatePassenger(@RequestBody Passenger passenger, @PathVariable Long id){

        return new ResponseEntity<>(new UserDTO(passengerService.getMockPassenger()), HttpStatus.OK);
    }



}
