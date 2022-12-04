package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/putnik")
public class PassengerController {

    @Autowired
    private PassengerService passengerService;
}
