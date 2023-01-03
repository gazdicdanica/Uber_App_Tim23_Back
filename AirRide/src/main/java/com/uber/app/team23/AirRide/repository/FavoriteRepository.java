package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.rideData.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
}
