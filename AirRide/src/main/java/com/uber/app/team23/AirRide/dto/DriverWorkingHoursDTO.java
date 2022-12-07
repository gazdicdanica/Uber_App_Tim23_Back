package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter @Getter @NoArgsConstructor
public class DriverWorkingHoursDTO {
    private double totalCount = 0.0;
    private List<WorkingHours> timeList = new ArrayList<>();

    public void updateWorkingHours(WorkingHours workingHours) {
        this.totalCount += (double)Duration.between(workingHours.getStart(), workingHours.getEnd()).toMinutes()/60;
        this.timeList.add(workingHours);
    }
}
