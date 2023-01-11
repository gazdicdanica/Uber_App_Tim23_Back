package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Review;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    private List<UserShortDTO> passenger = new ArrayList<>();

    public void addPassenger(UserShortDTO user) {
        this.passenger.add(user);
    }

    public ReviewDTO(Review review){
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        addPassenger(new UserShortDTO(review.getPassenger()));
    }
}
