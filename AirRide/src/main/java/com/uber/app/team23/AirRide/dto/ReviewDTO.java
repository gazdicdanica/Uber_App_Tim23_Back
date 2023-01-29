package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.rideData.Review;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ReviewDTO {
    private Long id;

    @NumberFormat
    private int rating;

    @NotNull
    private String comment;

    @Nullable
    @Valid
    private UserShortDTO passenger;

    public ReviewDTO(Review review){
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.passenger = new UserShortDTO(review.getPassenger());
    }
}
