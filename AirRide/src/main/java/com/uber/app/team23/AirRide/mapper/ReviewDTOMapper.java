package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.ReviewDTO;
import com.uber.app.team23.AirRide.model.rideData.Review;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public ReviewDTOMapper(ModelMapper modelMapper) { this.modelMapper = modelMapper; }

    public static ReviewDTO fromReviewToDTO(Review review) {
        return modelMapper.map(review, ReviewDTO.class);
    }
}
