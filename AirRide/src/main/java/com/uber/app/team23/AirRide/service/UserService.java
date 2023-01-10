package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    public User updateUserPassword(User user) {
        return userRepository.save(user);
    }

    public boolean isDriver(User user){
        for(Role role : user.getRole()){
            if(role.getName().equals("driver")){
                return true;
            }
        }
        return false;
    }

    public boolean isPassenger(User user){
        for(Role role : user.getRole()){
            if(role.getName().equals("passenger")){
                return true;
            }
        }
        return false;
    }
}
