package com.uber.app.team23.AirRide.controller;


import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.mapper.ReviewDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Review;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.service.DriverService;
import com.uber.app.team23.AirRide.service.ReviewService;
import com.uber.app.team23.AirRide.service.RideService;
import com.uber.app.team23.AirRide.service.VehicleService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController @RequestMapping(value = "/api/review", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private RideService rideService;
    @Autowired
    private DriverService driverService;

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/{rideId}/vehicle")
    public ResponseEntity<ReviewDTO> createReviewVehicle(@PathVariable Long rideId, @RequestBody ReviewDTO dto) {
        Ride ride = rideService.findOne(rideId);
        Review rev = new Review(dto, ride, true);
        rev = reviewService.save(rev);
        ReviewDTO response = new ReviewDTO(rev);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/vehicle/{id}")
    public ResponseEntity<ReviewLongDTO> getReviewsForVehicle(@PathVariable Long id) {
        Ride ride = rideService.findOne(id);
        Vehicle vehicle = ride.getVehicle();
//        Vehicle vehicle = vehicleService.findOne(id);
        List<ReviewDTO> reviewDTOS = reviewService.findAll(vehicle.getDriver(), true);

        return new ResponseEntity<>(new ReviewLongDTO(reviewDTOS), HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ReviewLongDTO> getReviewsForDriver(@PathVariable Long id) {
        List<ReviewDTO> reviewDTOS = reviewService.findAll(driverService.findById(id), false);

        return new ResponseEntity<>(new ReviewLongDTO(reviewDTOS), HttpStatus.OK);
    }

    @Transactional
//    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping(value = "/{rideId}/driver")
    public ResponseEntity<ReviewDTO> createReviewDriver(@PathVariable Long rideId, @Valid @RequestBody ReviewDTO dto) {
        Ride ride = rideService.findOne(rideId);
        driverService.findById(ride.getDriver().getId());
        Review rev = new Review(dto, ride, false);
        return new ResponseEntity<>(new ReviewDTO(reviewService.save(rev)), HttpStatus.OK);
    }


    @GetMapping(value = "/{rideId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ReviewExtraLongDTO> getAllReview(@PathVariable Long rideId) {
        Ride ride = rideService.findOne(rideId);
        List<ReviewDTO> vehicleReviews = reviewService.findAll(ride.getDriver(), true);
        List<ReviewDTO> driverReviews = reviewService.findAll(ride.getDriver(), false);

        return new ResponseEntity<>(new ReviewExtraLongDTO(vehicleReviews, driverReviews), HttpStatus.OK);
    }
}