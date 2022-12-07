package com.uber.app.team23.AirRide.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PanicPaginatedDTO {
    private int totalCount;
    private List<PanicDTO> results;

    public PanicPaginatedDTO(List<PanicDTO> panics){
        this.results = panics;
        this.totalCount = panics.size();
    }
}
