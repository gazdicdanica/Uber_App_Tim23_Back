package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.ReviewDTO;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.ReviewDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Review;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review save(Review rev) {
        return reviewRepository.save(rev);
    }


    public List<ReviewDTO> findAll(Driver driver, boolean isVehicleReview) {
        return reviewRepository.findAllByDriverAndReviewForVehicle(driver, isVehicleReview).stream().map(ReviewDTOMapper::fromReviewToDTO).toList();
    }
}

