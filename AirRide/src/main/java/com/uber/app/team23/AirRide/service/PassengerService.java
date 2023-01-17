package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.UserDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.messageData.EmailDetails;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.UserActivation;
import com.uber.app.team23.AirRide.repository.PassengerRepository;
import com.uber.app.team23.AirRide.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Autowired
//    private RideService rideService;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserActivationService userActivationService;

    @Autowired
    private EmailService emailService;

    public Passenger findByEmail(String email) {
        return passengerRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Passenger does not exist!"));
    }

    public Page<Passenger> findAll(Pageable pageable){
        return passengerRepository.findAll(pageable);
    }

    public Passenger findOne(Long id) {
        return passengerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Passenger does not exist!"));
    }

    public Passenger update(UserDTO p, Long id){
        Passenger passenger = this.findOne(id);
        passenger.setName(p.getName());
        passenger.setSurname(p.getSurname());
        passenger.setProfilePicture(Base64.getDecoder().decode(p.getProfilePicture()));
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

    public void sendActivationEmail(String email, Long activationId){
        EmailDetails details = new EmailDetails();
        details.setRecipient(email);
        details.setSubject("Activation for your AirRide account");
        emailService.sendActivationMail(details, activationId);
    }

    public UserActivation addActivation(Passenger passenger){
        return this.userActivationService.create(passenger);
    }

    public Passenger createPassenger(Passenger passenger) {

        Passenger existingPassenger = passengerRepository.findByEmail(passenger.getEmail()).orElse(null);
        if(existingPassenger != null){
            throw new BadRequestException("User with that email already exists!");
        }

        Passenger p = new Passenger();
        p.setEmail(passenger.getEmail());
        p.setPassword(passwordEncoder.encode(passenger.getPassword()));
        p.setName(passenger.getName());
        p.setSurname(passenger.getSurname());
        p.setTelephoneNumber(passenger.getTelephoneNumber());
        p.setProfilePicture(passenger.getProfilePicture());
        p.setAddress(passenger.getAddress());
        p.setBlocked(false);
        p.setActive(false);
        List<Role> li = new ArrayList<>();
        li.add(new Role(1L, "passenger"));
        p.setRole(li);

        return this.passengerRepository.save(p);
    }

    public Page<Ride> findAllRides(Long id, Pageable pageable){
        Passenger p = findOne(id);
        return rideRepository.findByPassengersContaining(p, pageable);
    }
}
