package com.uber.app.team23.AirRide.controller;


import com.uber.app.team23.AirRide.dto.PassengerShortDTO;
import com.uber.app.team23.AirRide.dto.ReviewDTO;
import com.uber.app.team23.AirRide.dto.ReviewExtraLongDTO;
import com.uber.app.team23.AirRide.dto.ReviewLongDTO;
import com.uber.app.team23.AirRide.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController @RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{rideId}/vehicle/{vehicleId}")
    public ResponseEntity<ReviewDTO> createReviewVehicle(@PathVariable Integer rideId, @PathVariable Integer vehicleId, @RequestBody ReviewDTO review) {
        PassengerShortDTO passenger = new PassengerShortDTO((long) 1, "email");
        ReviewDTO rev = new ReviewDTO();
        rev.setId((long) 123);
        rev.setRating(review.getRating());
        rev.setComment(review.getComment());
        rev.addPassenger(passenger);

        return new ResponseEntity<>(rev, HttpStatus.OK);
    }

    @GetMapping(value = "/driver/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewLongDTO> getReviewsForVehicle(@PathVariable Integer id) {
        ReviewLongDTO reviewLong = new ReviewLongDTO();

        PassengerShortDTO passenger = new PassengerShortDTO((long) 1, "email");
        ReviewDTO rev = new ReviewDTO();
        rev.setId((long) 123);
        rev.setComment("too fast");
        rev.setRating(3);

        // Returns list of passengers who left reviews in case multiple passengers were on ride
        rev.addPassenger(passenger);

        reviewLong.setTotalReviews(rev.getPassengers().size());
        reviewLong.updateReviewLi(rev);

        return new ResponseEntity<>(reviewLong, HttpStatus.OK);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{rideId}/driver/{id}")
    public ResponseEntity<ReviewDTO> createReviewDriver(@PathVariable Integer rideId, @PathVariable Integer id, @RequestBody ReviewDTO review) {
        ReviewDTO rev = new ReviewDTO();
        rev.setId((long) 123);
        rev.setRating(review.getRating());
        rev.setComment(review.getComment());
        rev.addPassenger(new PassengerShortDTO((long) 123, "email"));

        return new ResponseEntity<>(rev, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/{id}")
    public ResponseEntity<ReviewExtraLongDTO> getAllReview(@PathVariable Integer id) {
        PassengerShortDTO psngr = new PassengerShortDTO((long) 1, "email");
        ReviewDTO vehicleRev = new ReviewDTO();
        vehicleRev.setId((long) 123);
        vehicleRev.setRating(3);
        vehicleRev.setComment("com1");
        vehicleRev.addPassenger(psngr);

        ReviewDTO driverRev = new ReviewDTO();
        driverRev.setId((long) 123);
        driverRev.setRating(3);
        driverRev.setComment("com2");
        driverRev.addPassenger(psngr);

        ReviewExtraLongDTO rev = new ReviewExtraLongDTO();
        rev.updateDriverReviewsLi(driverRev);
        rev.updateVehicleReviewsLi(vehicleRev);

        return new ResponseEntity<>(rev, HttpStatus.OK);
    }
}
