package com.uber.app.team23.AirRide.model.users.driverData;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "working_hours")
public class WorkingHours {
    @Column(name = "start_time") @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime start;

    @Column(name = "end_time") @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
