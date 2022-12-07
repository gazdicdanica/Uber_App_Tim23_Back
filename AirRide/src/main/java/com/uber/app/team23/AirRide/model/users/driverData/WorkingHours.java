package com.uber.app.team23.AirRide.model.users.driverData;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @NoArgsConstructor
public class WorkingHours {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long driverId;

    public WorkingHours(LocalDateTime start, LocalDateTime end, Long driverId) {
        this.start = start;
        this.end = end;
        this.driverId = driverId;
    }
}
