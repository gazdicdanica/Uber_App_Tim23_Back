package com.uber.app.team23.AirRide.mapper;

import com.uber.app.team23.AirRide.dto.FavoriteDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.model.rideData.Favorite;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FavoriteDTOMapper {
    private static ModelMapper modelMapper;

    @Autowired
    public FavoriteDTOMapper(ModelMapper modelMapper) {this.modelMapper = modelMapper;}

    public static FavoriteDTO fromFavoriteToDTO(Favorite favorite) { return modelMapper.map(favorite, FavoriteDTO.class); }

}
