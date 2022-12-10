package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReviewLongDTO {
    private int totalCount;
    private List<ReviewDTO> results = new ArrayList<>();

    public void updateReviewLi(ReviewDTO review){
        this.results.add(review);
    }
}
