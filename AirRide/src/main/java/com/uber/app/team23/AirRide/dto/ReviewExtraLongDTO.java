package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
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
