package com.uber.app.team23.AirRide.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ReviewExtraLongDTO {
    private List<ReviewDTO> vehicleReviews = new ArrayList<>();
    private List<ReviewDTO> driverReviews = new ArrayList<>();

    public void updateVehicleReviewsLi(ReviewDTO review) {
        this.vehicleReviews.add(review);
    }

    public void updateDriverReviewsLi(ReviewDTO review) {
        this.driverReviews.add(review);
    }
}
