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

    public List<Passenger> getAll(){
        passengers.add(new Passenger((long)1, "Pera", "Peric", "1234", "+381123123", "pera.peric@email.com", "Bulevar", "123", false, true, null, null));
        passengers.add(new Passenger((long)2, "Pera", "Peric", "1234", "+381123123", "pera.peric@email.com", "Bulevar", "123", false, true, null, null));
        passengers.add(new Passenger((long)3, "Pera", "Peric", "1234", "+381123123", "pera.peric@email.com", "Bulevar", "123", false, true, null, null));
        passengers.add(new Passenger((long)4, "Pera", "Peric", "1234", "+381123123", "pera.peric@email.com", "Bulevar", "123", false, true, null, null));
        return passengers;
    }

    public Passenger create(Passenger passenger){
        //TODO persist data
        passengers.add(passenger);
        return passenger;
    }
}
