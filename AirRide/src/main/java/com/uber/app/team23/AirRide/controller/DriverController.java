package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/vozac")
public class DriverController {

    @Autowired
    private DriverService driverService;

}
