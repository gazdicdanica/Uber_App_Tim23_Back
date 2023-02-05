package com.uber.app.team23.AirRide.controller;

import com.uber.app.team23.AirRide.dto.PanicDTO;
import com.uber.app.team23.AirRide.dto.PanicPaginatedDTO;
import com.uber.app.team23.AirRide.service.PanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/api/panic")
public class PanicController {

    @Autowired
    PanicService panicService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_DRIVER', 'ROLE_USER')")
    public ResponseEntity<PanicPaginatedDTO> getAll(Pageable pageable){
        List<PanicDTO> panics = panicService.findAllDTO(pageable);
        return new ResponseEntity<>(new PanicPaginatedDTO(panics), HttpStatus.OK);
    }
}
