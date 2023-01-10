package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.RidePaginatedDTO;
import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.dto.UserPaginatedDTO;
import com.uber.app.team23.AirRide.mapper.PassengerDTOMapper;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.UserActivation;
import com.uber.app.team23.AirRide.service.PassengerService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController @RequestMapping(value = "api/passenger", produces = MediaType.APPLICATION_JSON_VALUE)
public class PassengerController {

    @Autowired
    private PassengerService passengerService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createPassenger(@Valid @RequestBody Passenger passenger) throws ConstraintViolationException {
        Passenger newPassenger = passengerService.createPassenger(passenger);
        UserActivation activation = passengerService.addActivation(newPassenger);
        System.err.println(activation.activationId);
        passengerService.sendActivationEmail(newPassenger.getEmail(), activation.getActivationId());
        return new ResponseEntity<>(new UserDTO(newPassenger), HttpStatus.OK);

    }

    @GetMapping(value ={"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getPassenger(@PathVariable("id") Long id){
        Passenger p = passengerService.findOne(id);
        return new ResponseEntity<>(new UserDTO(p), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<UserPaginatedDTO> getPassengersPage(Pageable page){
        Page<Passenger> passengersPage = passengerService.findAll(page);
        List<UserDTO> users = passengersPage.stream().map(PassengerDTOMapper::fromPassengerToDTO).collect(Collectors.toList());
        return new ResponseEntity<>(new UserPaginatedDTO(users), HttpStatus.OK);
    }

    // TODO
    @GetMapping("/{id}/ride")
    public ResponseEntity<RidePaginatedDTO> getPassengerRidesPage(@PathVariable Long id,  @RequestParam int page, @RequestParam int size,
                                                                  @RequestParam String sort, @RequestParam String from, @RequestParam String to)
    {
//        Page<Ride> ridePage =
        return new ResponseEntity<>(new RidePaginatedDTO(new ArrayList<>()), HttpStatus.OK);
    }

    @GetMapping("/activate/{activationId}")
    public ResponseEntity<String> activatePassengerAccount(@PathVariable Long activationId){
        passengerService.activatePassenger(activationId);
        JSONObject obj = new JSONObject();
        obj.put("message", "Successful account activation!");
        return new ResponseEntity<>(obj.toString(),HttpStatus.OK);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updatePassenger(@Valid @RequestBody UserDTO passenger, @PathVariable Long id){

        Passenger updatedPassenger = passengerService.update(passenger, id);
        return new ResponseEntity<>(new UserDTO(updatedPassenger), HttpStatus.OK);
    }



}
