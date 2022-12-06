package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.LoginDTO;
import com.uber.app.team23.AirRide.dto.TokensDTO;
import com.uber.app.team23.AirRide.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/login")
    public ResponseEntity<TokensDTO> userLogin(@RequestBody LoginDTO loginParams){
        return new ResponseEntity<>(new TokensDTO( "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVC"),
                HttpStatus.OK);
    }
}
