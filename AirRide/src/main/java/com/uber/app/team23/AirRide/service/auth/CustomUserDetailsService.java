package com.uber.app.team23.AirRide.service.auth;

import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(username);
        System.err.println("User iz Load-a: "+user.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", username));
        } else {
            return user;
        }
    }
}
