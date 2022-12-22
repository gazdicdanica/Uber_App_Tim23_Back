package com.uber.app.team23.AirRide.model.users.driverData;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "working_hours")
public class WorkingHours {
    @Column(name = "start_time")
    private LocalDateTime start;
    @Column(name = "end_time")
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
