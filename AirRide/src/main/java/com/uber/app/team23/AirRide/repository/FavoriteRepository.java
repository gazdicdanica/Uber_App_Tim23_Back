package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.dto.FavoriteDTO;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
