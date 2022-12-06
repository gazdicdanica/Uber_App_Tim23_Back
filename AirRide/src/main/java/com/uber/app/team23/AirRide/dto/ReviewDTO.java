package com.uber.app.team23.AirRide.dto;

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
    private List<PassengerShortDTO> passengers = new ArrayList<PassengerShortDTO>();

    public void addPassenger(PassengerShortDTO psngr) {
        this.passengers.add(psngr);
    }
}
