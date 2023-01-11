package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.mapper.WorkHoursDTOMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReviewLongDTO {
    private int totalCount;
    private List<ReviewDTO> results = new ArrayList<>();

    public void updateReviewLi(ReviewDTO review){
        this.results.add(review);
    }

    public ReviewLongDTO(List<ReviewDTO> results) {
        this.results = results;
        this.totalCount = results.size();
    }
}

