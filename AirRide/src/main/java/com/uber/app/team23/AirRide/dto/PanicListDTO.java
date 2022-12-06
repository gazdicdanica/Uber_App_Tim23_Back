package com.uber.app.team23.AirRide.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PanicListDTO {
    private int totalCount;
    private List<PanicDTO> entries;

    public PanicListDTO(List<PanicDTO> panics){
        this.entries = panics;
        this.totalCount = panics.size();
    }
}
