package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.ReviewDTO;
import com.uber.app.team23.AirRide.model.rideData.Review;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.uber.app.team23.AirRide.dto.ReviewDTO(r) from Review r where r.driver=?1 and r.reviewForVehicle=true ")
    List<ReviewDTO> findAllVehicleReviewsByDriver(Driver driver);

    @Query("select new com.uber.app.team23.AirRide.dto.ReviewDTO(r) from Review r where r.driver=?1 and r.reviewForVehicle=false ")
    List<ReviewDTO> findAllDriverReviewsByDriver(Driver driver);
}
