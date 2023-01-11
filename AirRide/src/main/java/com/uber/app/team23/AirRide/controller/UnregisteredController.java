package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.EstimationDTO;
import com.uber.app.team23.AirRide.dto.UnregisteredRideRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping(value = "/api/unregisteredUser/", produces = MediaType.APPLICATION_JSON_VALUE)
public class UnregisteredController {

    @PostMapping
    public ResponseEntity<EstimationDTO> getRideEstimation(@RequestBody UnregisteredRideRequestDTO request){
        return new ResponseEntity<>(new EstimationDTO(10, 450), HttpStatus.OK);
    }

}
