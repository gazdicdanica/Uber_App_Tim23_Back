package com.uber.app.team23.AirRide.controller;


import com.uber.app.team23.AirRide.Utils.TokenUtils;
import com.uber.app.team23.AirRide.dto.LoginDTO;
import com.uber.app.team23.AirRide.dto.TokensDTO;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokensDTO> createAuthenticationToken(
            @RequestBody LoginDTO authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User u = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(u.getEmail(), u.getId(), u.getAuthorities());
        int expiresIn = tokenUtils.getExpiredIn();

        System.err.println("Ulogovan");

        return ResponseEntity.ok(new TokensDTO(jwt, (long) expiresIn));
    }
}
