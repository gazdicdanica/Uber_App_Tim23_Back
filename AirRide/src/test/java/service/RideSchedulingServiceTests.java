package service;

import com.uber.app.team23.AirRide.AirRideApplication;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.RideRepository;
import com.uber.app.team23.AirRide.service.DriverService;
import com.uber.app.team23.AirRide.service.RideSchedulingService;
import com.uber.app.team23.AirRide.service.WorkingHoursService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = AirRideApplication.class)
public class RideSchedulingServiceTests {

    @Autowired
    private RideSchedulingService rideSchedulingService;
    @MockBean
    private RideRepository rideRepository;
    @MockBean
    private DriverService driverService;
    @MockBean
    private WorkingHoursService workingHoursService;

    private static final Long VALID_ID = 1L;

    private Ride ride = new Ride();

    @Test
    public void test_FindDriver_NoOnlineDrivers(){
        when(driverService.findOnlineDrivers()).thenReturn(new ArrayList<>());
        Exception e = assertThrows(BadRequestException.class, () -> rideSchedulingService.findDriver(new Ride()));
        assertEquals("No drivers are online.", e.getMessage());
    }

    @Test
    public void test_FindDDriver_NoAppropriateVehicle(){
        when(driverService.findOnlineDrivers()).thenReturn(generateDrivers());
        Exception e = assertThrows(BadRequestException.class, () -> rideSchedulingService.findDriver(new Ride()));
        assertEquals("No driver is online with appropriate vehicle.", e.getMessage());
    }

    @Test
    public void test_FindDriver_WorkHours(){
        setUpRide();
        List<Driver> drivers = setUpDriverVehicle();
        when(driverService.findOnlineDrivers()).thenReturn(drivers);
        when(workingHoursService.calculateWorkingHours(any(Driver.class))).thenReturn(9);
        Exception e = assertThrows(BadRequestException.class, () -> rideSchedulingService.findDriver(ride));
        assertEquals("No driver is available at the moment.", e.getMessage());
    }

    @Test
    public void test_FindDriver_AllOccupied(){
        setUpRide();
        List<Driver> drivers = setUpDriverVehicle();
        when(driverService.findOnlineDrivers()).thenReturn(drivers);
        for(Driver d : drivers){
            when(driverService.findById(VALID_ID)).thenReturn(d);
            when(rideRepository.findByDriverAndStatus(d, RideStatus.ACTIVE)).thenReturn(Optional.of(new Ride()));
            when(rideRepository.findByDriverAndStatus(d, RideStatus.ACCEPTED)).thenReturn(Optional.of(new Ride()));
            when(workingHoursService.calculateWorkingHours(d)).thenReturn(2);

        }
        BadRequestException e = assertThrows(BadRequestException.class, () -> rideSchedulingService.findDriver(ride));
        assertEquals("No driver is available at the moment.", e.getMessage());

    }

    @Test
    public void findDriver_Fastest_AllAvailable(){
        setUpRide();
        List<Driver> drivers = setUpDriverVehicle();
        when(driverService.findOnlineDrivers()).thenReturn(drivers);
        for(Driver d : drivers){
            when(driverService.findById(VALID_ID)).thenReturn(d);
            when(rideRepository.findByDriverAndStatus(d, RideStatus.ACTIVE)).thenReturn(Optional.empty());
            when(rideRepository.findByDriverAndStatus(d, RideStatus.ACCEPTED)).thenReturn(Optional.empty());
            when(workingHoursService.calculateWorkingHours(d)).thenReturn(2);
        }
        RideSchedulingService mockService = mock(RideSchedulingService.class);
        doReturn(Arrays.asList(10.0)).when(mockService).getEstimates(any(Location.class), any(Location.class));
        Driver fastestDriver = rideSchedulingService.findDriver(ride);
        assertNotNull(fastestDriver);
        assertEquals(drivers.get(0), fastestDriver);
    }

    @Test
    public void findDriver_Fastest_AllInRide(){
        setUpRide();
        List<Driver> drivers = setUpDriverVehicle();
        when(driverService.findOnlineDrivers()).thenReturn(drivers);
        for(Driver d : drivers){
            when(driverService.findById(VALID_ID)).thenReturn(d);
            when(rideRepository.findByDriverAndStatus(d, RideStatus.ACTIVE)).thenReturn(Optional.of(ride));
            when(rideRepository.findByDriverAndStatus(d, RideStatus.ACCEPTED)).thenReturn(Optional.empty());
            when(workingHoursService.calculateWorkingHours(d)).thenReturn(2);
        }
        RideSchedulingService mockService = mock(RideSchedulingService.class);
        doReturn(Arrays.asList(10.0)).when(mockService).getEstimates(any(Location.class), any(Location.class));
        Driver fastestDriver = rideSchedulingService.findDriver(ride);
        assertNotNull(fastestDriver);
        assertEquals(drivers.get(0), fastestDriver);
    }


    private List<Driver> generateDrivers(){
        List<Driver> drivers = new ArrayList<>();
        for(int i = 0; i< 3; i++){
            Driver driver = new Driver();
            driver.setVehicle(new Vehicle());
            drivers.add(driver);
            VehicleType vt = new VehicleType();
            vt.setType(VehicleEnum.VAN);
            driver.getVehicle().setVehicleType(vt);
            driver.getVehicle().setCurrentLocation(new Location(10.0 + i , 10.0 + i));

        }
        return drivers;

    }

    private List<Driver> setUpDriverVehicle(){
        List<Driver> drivers = generateDrivers();
        for(Driver d : drivers){
            d.setId(VALID_ID);
            Vehicle v = d.getVehicle();
            v.setVehicleType(new VehicleType());
            v.getVehicleType().setType(VehicleEnum.STANDARD);
            v.setBabyTransport(false);
            v.setPetTransport(true);

        }
        return drivers;
    }

    private void setUpRide(){
        Route route = new Route();
        route.setDeparture(new Location(11.11, 11.11, "Address"));
        route.setDestination(new Location(11.10,11.10, "Address 2"));
        route.setDistance(5.0);
        ride.setLocations(Arrays.asList(route));
        ride.setVehicleType(VehicleEnum.STANDARD);
        ride.setBabyTransport(false);
        ride.setPetTransport(true);
    }
}
