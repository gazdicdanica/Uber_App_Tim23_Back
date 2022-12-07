package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.PanicDTO;
import com.uber.app.team23.AirRide.dto.PanicPaginatedDTO;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.users.Passenger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController @RequestMapping("/api/panic")
public class PanicController {

    @GetMapping
    public ResponseEntity<PanicPaginatedDTO> getAll(){
        List<PanicDTO> panics = new ArrayList<>();
        Passenger p = new Passenger();
        p.setId((long)1);
        panics.add(new PanicDTO(new Panic((long)1, p,null, LocalDateTime.now(), "reason")));
        return(new ResponseEntity<>(new PanicPaginatedDTO(panics), HttpStatus.OK));
    }
}
