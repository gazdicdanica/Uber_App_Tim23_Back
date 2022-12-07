package com.uber.app.team23.AirRide.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class RidePaginatedDTO {
    private int totalCount;
    private List<RideResponseDTO> results;

    public RidePaginatedDTO(List<RideResponseDTO> results){
        this.results = results;
        this.totalCount = results.size();
    }
}
