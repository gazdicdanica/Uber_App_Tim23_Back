package com.uber.app.team23.AirRide.model.rideData;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor @AllArgsConstructor
@Entity @Table(name = "locations")
public class Location {
    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "longitude")
    @NotNull
    @DecimalMin(value="-180.0", message = "Longitude value must be between -180 and 180")
    @DecimalMax(value="180.0", message = "Longitude value must be between -180 and 180")
    public double longitude;

    @Column(name = "latitude")
    @NotNull
    @DecimalMin(value="-90.0", message = "Longitude value must be between -90 and 90")
    @DecimalMax(value="90.0", message = "Longitude value must be between -90 and 90")
    public double latitude;

    @Column(name = "address")
    @NotNull @NotEmpty
    public String address;

}

