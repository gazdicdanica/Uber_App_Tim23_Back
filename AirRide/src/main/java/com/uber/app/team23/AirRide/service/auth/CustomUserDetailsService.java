package com.uber.app.team23.AirRide.service.auth;

import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.UserRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service @NoArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private User user;
    @Autowired
    private UserRepository userRepository;


    public CustomUserDetailsService(User user) {
        this.user = user;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }
}
