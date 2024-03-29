package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.PanicDTO;
import com.uber.app.team23.AirRide.mapper.PanicDTOMapper;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.repository.PanicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional
@Service
public class PanicService {
    @Autowired
    PanicRepository panicRepository;

    public PanicDTO save(Panic panic, Ride ride){
        Panic p = new Panic();
        p.setReason(panic.getReason());
        p.setCurrentRide(ride);
        p.setTime(LocalDateTime.now());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        p.setUser(user);
        return new PanicDTO(panicRepository.save(p));
    }

    public List<PanicDTO> findAllDTO(Pageable pageable){
        Page<Panic> panics = panicRepository.findAll(pageable);
        List<PanicDTO> ret = panics.stream().map(PanicDTOMapper::fromPanicToDTO).toList();
        return ret;
    }
}
