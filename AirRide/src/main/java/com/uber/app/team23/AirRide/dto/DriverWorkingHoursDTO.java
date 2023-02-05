package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor
public class DriverWorkingHoursDTO {
    private double totalCount = 0.0;
    private List<WorkHoursDTO> results = new ArrayList<>();

    public DriverWorkingHoursDTO(List<WorkHoursDTO> results) {
        this.results = results;
        this.totalCount = results.size();
    }
}
