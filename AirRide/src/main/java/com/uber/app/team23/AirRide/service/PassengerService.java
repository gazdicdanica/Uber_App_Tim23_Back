package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

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

    public Passenger findByEmail(String email) throws UsernameNotFoundException {
        return passengerRepository.findByEmail(email);
    }

    public Passenger findById(Long id) throws AccessDeniedException {
        return passengerRepository.findById(id).orElseGet(null);
    }

    public List<Passenger> findAll() throws AccessDeniedException {
        return passengerRepository.findAll();
    }

    public Passenger save(User u) {
        Passenger p = new Passenger();
        p.setEmail(u.getEmail());
        p.setPassword(passwordEncoder.encode(u.getPassword()));
        p.setName(u.getName());
        p.setLastName(u.getLastName());
        p.setPhoneNumber(u.getPhoneNumber());
        p.setProfilePhoto(u.getProfilePhoto());
        p.setAddress(u.getAddress());
        p.setBlocked(false);
        p.setActive(false);
        List<Role> li = new ArrayList<>();
        li.add(new Role(1L, "ROLE_USER"));
        p.setRole(li);
        return this.passengerRepository.save(p);
    }
}
