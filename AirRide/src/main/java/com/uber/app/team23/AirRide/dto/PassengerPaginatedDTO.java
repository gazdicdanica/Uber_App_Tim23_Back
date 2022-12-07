package com.uber.app.team23.AirRide.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class PassengerPaginatedDTO {
    private int totalCount;
    private List<PassengerDTO> results;

    public PassengerPaginatedDTO(List<PassengerDTO> results){
        this.results = results;
        this.totalCount = results.size();
    }
}
