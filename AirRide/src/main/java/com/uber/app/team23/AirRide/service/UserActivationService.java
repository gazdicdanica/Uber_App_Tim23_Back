package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.UserActivation;
import com.uber.app.team23.AirRide.repository.UserActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserActivationService {
    @Autowired
    private UserActivationRepository userActivationRepository;

    public UserActivation findOne(Long id){
        return userActivationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Activation with entered id does not exist"));
    }

    public UserActivation create(User user){
        UserActivation activation = new UserActivation();
        activation.setUser(user);
        activation.setCreationDT(LocalDateTime.now());
        activation.setLifespan(activation.getCreationDT().plusMinutes(15));

        return userActivationRepository.save(activation);
    }

    public boolean isExpired(UserActivation userActivation){
        LocalDateTime now = LocalDateTime.now();
        return (now.isAfter(userActivation.lifespan));
    }
}
