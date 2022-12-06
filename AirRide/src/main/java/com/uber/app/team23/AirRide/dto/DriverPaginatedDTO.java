package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
public class DriverPaginatedDTO {
    private int totalCount;
    private List<DriverDTO> results = new ArrayList<>();

    public void addDriver(DriverDTO driverDTO) {
        this.results.add(driverDTO);
    }

    public DriverPaginatedDTO(DriverDTO driverDTO){
        this.totalCount += 1;
        addDriver(driverDTO);
    }
}
