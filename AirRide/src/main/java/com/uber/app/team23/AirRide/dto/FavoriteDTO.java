package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.mapper.PassengerDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FavoriteDTO {
    private Long id;

    @NotEmpty @NotNull
    private String favoriteName;

    @Valid
    private Set<Route> locations;

    @Valid
    private Set<UserShortDTO> passengers;

    @NotNull
    private VehicleEnum vehicleType;

    @NotNull
    private boolean babyTransport;

    @NotNull
    private boolean petTransport;


    public FavoriteDTO(Favorite favorite){
        FavoriteDTO f = new FavoriteDTO();
        f.setId(favorite.getId());
        f.setFavoriteName(favorite.getFavoriteName());
        f.setLocations(favorite.getLocations());
        f.setVehicleType(favorite.getVehicleType());
        f.setBabyTransport(favorite.isBabyTransport());
        f.setPetTransport(favorite.isPetTransport());
        f.setPassengers(favorite.getPassengers().stream().map(PassengerDTOMapper::fromPassengerToShortDTO).collect(Collectors.toSet()));
    }

}
