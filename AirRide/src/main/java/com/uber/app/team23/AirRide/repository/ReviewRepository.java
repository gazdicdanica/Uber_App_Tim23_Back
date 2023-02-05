package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.ReviewDTO;
import com.uber.app.team23.AirRide.model.rideData.Review;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByRideAndReviewForVehicle(Ride ride, boolean forVehicle);

    List<Review> findAllByDriverAndReviewForVehicle(Driver driver, boolean forVehicle);

}
