package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.DateRangeDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.service.ReportService;
import com.uber.app.team23.AirRide.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/report", produces= MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    @Autowired
    ReportService reportService;

    @Transactional
    @GetMapping
    public ResponseEntity<List<RideResponseDTO>> getRidesForDateRange(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start, @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        List<RideResponseDTO> rides = reportService.getRidesForDateRange(start, end);
        return new ResponseEntity<>(rides, HttpStatus.OK);
    }
}
