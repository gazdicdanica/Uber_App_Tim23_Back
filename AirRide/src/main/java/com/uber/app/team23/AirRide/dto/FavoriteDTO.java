package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.mapper.PassengerDTOMapper;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor @AllArgsConstructor
public class FavoriteDTO {
    private Long id;

//    @NotEmpty @NotNull
    private String favoriteName;

    @Valid
    private List<Route> locations;

    @Valid
    private List<UserShortDTO> passengers;

//    @NotNull
    private VehicleEnum vehicleType;

//    @NotNull
    private boolean babyTransport;

//    @NotNull
    private boolean petTransport;


    public FavoriteDTO(Favorite favorite){
        this.id = favorite.getId();
        this.favoriteName = favorite.getFavoriteName();
        this.locations = favorite.getLocations();
        this.vehicleType = favorite.getVehicleType();
        this.babyTransport = favorite.isBabyTransport();
        this.petTransport = favorite.isPetTransport();
        this.passengers = favorite.getPassengers().stream().map(PassengerDTOMapper::fromPassengerToShortDTO).collect(Collectors.toList());
    }

}
