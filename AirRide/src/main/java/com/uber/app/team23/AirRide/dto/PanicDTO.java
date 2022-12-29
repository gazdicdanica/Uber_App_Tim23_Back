package com.uber.app.team23.AirRide.dto;

import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter @AllArgsConstructor
public class PanicDTO {
    private int id;
    private UserDTO user;
    private RideResponseDTO ride;
    private LocalDateTime time;
    private String reason;

    public PanicDTO(Panic panic){
        this(panic.id.intValue(), new UserDTO(panic.getUser()), null,
                panic.getTime(), panic.getReason());
        Ride r = new Ride((long)1, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), 1234, null, 10, null, null,
                RideStatus.ACTIVE, null, false, true, true, null, VehicleEnum.STANDARD,null);
        Driver d = new Driver();
        d.setId((long)1);
        d.setEmail("test@gmail.com");
        r.setDriver(d);
        ArrayList<UserShortDTO> passengers= new ArrayList<>();
        passengers.add(new UserShortDTO(1, "email"));
        Vehicle v = new Vehicle();
        v.setVehicleType(new VehicleType((long)1, VehicleEnum.STANDARD, 123));
        r.setVehicle(v);
        ArrayList<Route> locations = new ArrayList<>();
        locations.add(new Route());
        this.ride = new RideResponseDTO(r);
    }
}
