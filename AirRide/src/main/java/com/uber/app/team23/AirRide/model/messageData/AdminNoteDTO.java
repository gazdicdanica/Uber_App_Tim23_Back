package com.uber.app.team23.AirRide.model.messageData;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class AdminNoteDTO {
    @NotNull @NotEmpty @Size(min = 3, max = 30)
    private String message;
}
