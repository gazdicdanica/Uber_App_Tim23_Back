package com.uber.app.team23.AirRide.controller;


import com.uber.app.team23.AirRide.Utils.TokenUtils;
import com.uber.app.team23.AirRide.dto.LoginDTO;
import com.uber.app.team23.AirRide.dto.TokensDTO;
import com.uber.app.team23.AirRide.dto.UpdatePasswordDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.users.Role;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<TokensDTO> createAuthenticationToken(
            @RequestBody LoginDTO authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User u = (User) authentication.getPrincipal();
        if (u.isActive() && !u.isBlocked()){

            List<String> roles = new ArrayList<>();
            for(Object r : u.getAuthorities()){
                Role role = (Role) r;
                roles.add(role.getAuthority());
            }

            String jwt = tokenUtils.generateToken(u.getEmail(), u.getId(), roles);
            int expiresIn = tokenUtils.getExpiredIn();

            System.err.println("Ulogovan");

            return ResponseEntity.ok(new TokensDTO(jwt, (long) expiresIn));

        } else if (!u.isActive()){
            throw new EntityNotFoundException("Your Account Might Not Be Activated");
        } else {
            throw new BadRequestException("You Might Have Been Blocked By Administration");
        }
    }
    @PutMapping("/user/{id}/changePassword")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody UpdatePasswordDTO dto){
        User u = userService.findById(id);
        if (u == null) {
            throw new EntityNotFoundException("User With This ID Does Not Exist");
        } else if (!passwordEncoder.matches(dto.getOld_password(), u.getPassword())) {
            throw new BadRequestException("Current password is not matching!");
        } else {
            u.setPassword(passwordEncoder.encode(dto.getNew_password()));
            userService.updateUserPassword(u);
        }
        return new ResponseEntity<>("Password successfully changed!", HttpStatus.NO_CONTENT);
    }
}
