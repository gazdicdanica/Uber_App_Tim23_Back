package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.Passenger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {
    List<Passenger> passengers = new ArrayList<Passenger>();

    public Passenger get(Long id){
        for(Passenger p : passengers){
            if (p.getId().equals(id))
                return p;
        }
        return null;
    }

    public Passenger create(Passenger passenger){
        //TODO persist data
        passengers.add(passenger);
        return passenger;
    }
}
