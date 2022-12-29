package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.UserActivation;
import com.uber.app.team23.AirRide.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    private UserActivationService userActivationService;

    public Passenger findByEmail(String email) {
        return passengerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Passenger does not exist!"));
    }

    public Page<Passenger> findAll(Pageable pageable){
        return passengerRepository.findAll(pageable);
    }

    public Passenger findOne(Long id) {
        return passengerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Passenger does not exist!"));
    }

    public Passenger update(Passenger p, Long id){
        Passenger passenger = this.findOne(id);
        passenger.setName(p.getName());
        passenger.setSurname(p.getSurname());
        passenger.setProfilePicture(p.getProfilePicture());
        passenger.setTelephoneNumber(p.getTelephoneNumber());
        passenger.setAddress(p.getAddress());
        return passengerRepository.save(passenger);
    }

    public void activatePassenger(Long activationId){
        UserActivation activation = this.userActivationService.findOne(activationId);

        if(userActivationService.isExpired(activation)){
            throw new BadRequestException("Activation expired. Register again!");
        }

        Passenger passenger = this.findOne(activation.getUser().getId());
        passenger.setActive(true);
        passengerRepository.save(passenger);
    }

    public Passenger save(User u) {

        Passenger existingPassenger = passengerRepository.findByEmail(u.getEmail()).orElse(null);
        if(existingPassenger != null){
            throw new BadRequestException("User with that email already exists");
        }

        Passenger p = new Passenger();
        p.setEmail(u.getEmail());
        p.setPassword(passwordEncoder.encode(u.getPassword()));
        p.setName(u.getName());
        p.setSurname(u.getSurname());
        p.setTelephoneNumber(u.getTelephoneNumber());
        p.setProfilePicture(u.getProfilePicture());
        p.setAddress(u.getAddress());
        p.setBlocked(false);
        p.setActive(false);
        List<Role> li = new ArrayList<>();
        li.add(new Role(1L, "ROLE_USER"));
        p.setRole(li);
        return this.passengerRepository.save(p);
    }
}
