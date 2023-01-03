package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.PanicDTO;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.repository.PanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PanicService {
    @Autowired
    PanicRepository panicRepository;

    public PanicDTO save(Panic panic, Ride ride){
        Panic p = new Panic();
        p.setReason(panic.getReason());
        p.setCurrentRide(ride);
        p.setTime(LocalDateTime.now());
        //TODO set user from token
        return new PanicDTO(panicRepository.save(p));
    }
}
