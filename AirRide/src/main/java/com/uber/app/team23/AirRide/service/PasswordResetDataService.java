package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.users.PasswordResetData;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.PasswordResetDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetDataService {

    @Autowired
    private PasswordResetDataRepository passwordResetDataRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordResetData save(PasswordResetData passwordResetData) {
        return passwordResetDataRepository.save(passwordResetData);
    }

    public User resetPassword(PasswordResetData dto, User user) {
        PasswordResetData prd = passwordResetDataRepository.findByCode(dto.getCode()).orElse(null);
        if (prd == null) {
            throw new EntityNotFoundException("Wrong Code");
        }
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        passwordResetDataRepository.deleteById(prd.getId());

        return user;
    }
}
