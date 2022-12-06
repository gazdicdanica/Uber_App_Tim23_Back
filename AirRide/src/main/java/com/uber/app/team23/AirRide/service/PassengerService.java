package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.Passenger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {
    List<Passenger> passengers = new ArrayList<Passenger>();

    public Passenger getMockPassenger(){
        Passenger p = new Passenger();
        p.setId((long)1);
        p.setName("Pera");
        p.setLastName("Peric");
        p.setPassword("sifra123");
        p.setAddress("Bulevar Oslobodjenja 47");
        p.setPhoneNumber("+381123123");
        p.setProfilePhoto("profilna");
        p.setEmail("test@email.com");
        return p;
    }
}
