package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.messageData.EmailDetails;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.PasswordResetData;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.RideRepository;
import com.uber.app.team23.AirRide.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetDataService passwordResetDataService;



    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Page<User> findAll(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public User updateUserPassword(User user) {
        return userRepository.save(user);
    }

    public boolean isDriver(User user){
        for(Role role : user.getRole()){
            if(role.getName().equals("ROLE_DRIVER")){
                return true;
            }
        }
        return false;
    }

    public boolean isPassenger(User user){
        for(Role role : user.getRole()){
            if(role.getName().equals("ROLE_USER")){
                return true;
            }
        }
        return false;
    }

    public User save(User u) {
        return userRepository.save(u);
    }

    public void sendResetPwCode(User u) {
        EmailDetails details = new EmailDetails();
        details.setRecipient(u.getEmail());
        details.setSubject("Your Reset Password Code");
        emailService.sendResetPwCode(details, generateResetPwToken(), u.getId());
    }

    public String generateResetPwToken() {
        return RandomStringUtils.randomAlphanumeric(16);
    }

    public void resetPassword(PasswordResetData dto) {
        User u = passwordResetDataService.resetPassword(dto, userRepository.findByEmail(dto.getEmail()));
        userRepository.save(u);
    }
}
