package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.PanicDTO;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PanicRepository extends JpaRepository<Panic, Long> {

    @Query(value = "select new com.uber.app.team23.AirRide.dto.PanicDTO(p) from Panic p")
    List<PanicDTO> findAllDTO();
}
