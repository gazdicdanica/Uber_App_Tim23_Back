package service;

import com.uber.app.team23.AirRide.AirRideApplication;
import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Vehicle;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.*;
import com.uber.app.team23.AirRide.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = AirRideApplication.class)
public class RideServiceTests {
    @Autowired
    private RideService rideService;
    @MockBean
    private RideRepository rideRepository;
    @MockBean
    private PassengerService passengerService;
    @MockBean
    private RouteService routeService;
    @MockBean
    private RideSchedulingService rideSchedulingService;
    @MockBean
    private VehicleTypeRepository vehicleTypeRepository;
    @MockBean
    private RejectionRepository rejectionRepository;
    @MockBean
    private DriverService driverService;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private VehicleRepository vehicleRepository;

    private static Long VALID_ID = 1L;
    private static Long INVALID_ID = 1234L;

    private static String VALID_EMAIL = "test@email.com";
    private static String INVALID_EMAIL = "aaaaaaaaaaa";
    private static String DRIVER_ERROR = "Driver does not exist";
    private static String PASSENGER_ERROR = "Passenger does not exist!";

    private static String WITHDRAW_ERROR = "Cannot cancel a ride that is not in status PENDING or ACCEPTED";
    private static String START_ERROR = "Cannot start a ride that is not in status ACCEPTED!";

    private static String ACCEPT_ERROR = "Cannot accept a ride that is not in status PENDING!";

    private static String END_ERROR = "Cannot end a ride that is not in status ACTIVE!";

    private static String CANCEL_ERROR = "Cannot cancel a ride that is not in status PENDING or ACCEPTED!";
    private static String PANIC_ERROR = "Cannot panic a ride that is not in status ACCEPTED or ACTIVE";

    Ride ride = new Ride();
    Driver driver = new Driver();
    Passenger passenger = new Passenger();

    @Test
    public void test_FindOne_InvalidId(){
        Mockito.when(rideRepository.findById(VALID_ID)).thenReturn(Optional.of(ride));
        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> rideService.findOne(INVALID_ID));
        assertEquals("Ride does not exist", e.getMessage());
    }

    @Test
    public void test_FindOne_ValidId(){
        Mockito.when(rideRepository.findById(VALID_ID)).thenReturn(Optional.of(ride));
        ride.setId(VALID_ID);
        Ride found = rideService.findOne(VALID_ID);
        assertEquals(ride, found);
    }

    @Test
    public void test_FindActiveRideByDriver_InvalidId(){
        setUpForDriver();
        Exception e = assertThrows(EntityNotFoundException.class, ()-> rideService.findActiveByDriver(INVALID_ID));
        assertEquals(DRIVER_ERROR, e.getMessage());
    }

    @Test
    public void test_FindActiveRideByDriver_ValidId_NotNull(){
        setUpForDriver();
        RideResponseDTO dto = rideService.findActiveByDriver(VALID_ID);
        assertEquals(new RideResponseDTO(ride), dto);
    }

    @Test
    public void test_FindActiveRideByDriver_ValidId_Null(){
        Mockito.when(driverService.findById(VALID_ID)).thenReturn(driver);
        RideResponseDTO dto= rideService.findActiveByDriver(VALID_ID);
        assertNull(dto);
    }

    @Test
    public void test_FindActiveRideByPassenger_ValidId_NotNull(){
        setUpForPassenger();
        RideResponseDTO dto = rideService.findActiveByPassenger(VALID_ID);
        assertEquals(new RideResponseDTO(ride), dto);
    }

    @Test
    public void test_FindActiveRideByPassenger_InvalidId(){
        setUpForPassenger();
        Exception e = assertThrows(EntityNotFoundException.class, ()-> rideService.findActiveByPassenger(INVALID_ID));
        assertEquals(PASSENGER_ERROR, e.getMessage());
    }

    @Test
    public void test_CheckIfPassengerCanCreateRide_InvalidId(){
        setUpForPassenger();
        Exception e = assertThrows(EntityNotFoundException.class, ()->rideService.checkPassengerRide(INVALID_ID));
        assertEquals(PASSENGER_ERROR, e.getMessage());
    }

    @Test
    public void test_CheckIfPassengerCanCreateRide_ValidId_HasPending(){
        setUpForPassenger();
        List<Ride> li = new ArrayList<>();
        li.add(ride);
        Mockito.when(rideRepository.findByPassengersContainingAndStatus(passenger, RideStatus.PENDING)).thenReturn(li);
        Exception e = assertThrows(BadRequestException.class, () -> rideService.checkPassengerRide(VALID_ID));
        assertEquals("Cannot create a ride while you have one already pending!", e.getMessage());
    }

    @Test
    public void test_CheckIfPassengerCanCreateRide_ValidId_HasActive(){
        setUpForPassenger();
        List<Ride> li = new ArrayList<>();
        li.add(ride);
        Mockito.when(rideRepository.findByPassengersContainingAndStatus(passenger, RideStatus.ACCEPTED)).thenReturn(li);
        Exception e = assertThrows(BadRequestException.class, () -> rideService.checkPassengerRide(VALID_ID));
        assertEquals("Cannot create a ride while you have one already pending!", e.getMessage());

    }

    @Test
    public void test_CheckIfPassengerCanCreateRide_ValidId_NoRide(){
        setUpForPassenger();
        assertTrue(rideService.checkPassengerRide(VALID_ID));
    }

    @Test
    public void test_CheckIfPassengerCanCreateRideByEmail_Invalid(){
        setUpForPassenger();
        Exception e = assertThrows(EntityNotFoundException.class, () -> rideService.checkPassengerRideByEmail(INVALID_EMAIL));
        assertEquals(PASSENGER_ERROR, e.getMessage());
    }

    @Test
    public void test_CheckIfPassengerCanCreateRideByEmail_Valid_HasActive(){
        setUpForPassenger();
        List<Ride> li = new ArrayList<>();
        li.add(ride);
        Mockito.when(rideRepository.findByPassengersContainingAndStatus(passenger, RideStatus.ACCEPTED)).thenReturn(li);
        Exception e = assertThrows(BadRequestException.class, () -> rideService.checkPassengerRideByEmail(VALID_EMAIL));
        assertEquals("Cannot create a ride while you have one already pending!", e.getMessage());

    }

    @Test
    public void test_CheckIfPassengerCanCreateRideByEmail_Valid_HasPending(){
        setUpForPassenger();
        List<Ride> li = new ArrayList<>();
        li.add(ride);
        Mockito.when(rideRepository.findByPassengersContainingAndStatus(passenger, RideStatus.PENDING)).thenReturn(li);
        Exception e = assertThrows(BadRequestException.class, () -> rideService.checkPassengerRideByEmail(VALID_EMAIL));
        assertEquals("Cannot create a ride while you have one already pending!", e.getMessage());
    }

    @Test
    public void test_CheckIfPassengerCanCreateRideByEmail_Valid_NoRide(){
        setUpForPassenger();
        assertTrue(rideService.checkPassengerRideByEmail(VALID_EMAIL));
    }

    // WITHDRAW

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"PENDING", "ACCEPTED"})
    public void test_WithdrawRide_ValidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        RideResponseDTO response = rideService.withdrawRide(VALID_ID);
        assertEquals(VALID_ID, response.getId());
        assertEquals(RideStatus.CANCELED, response.getStatus());
        verify(rideRepository, times(1)).findById(VALID_ID);
        verify(rideRepository, times(1)).save(ride);

    }

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"REJECTED", "ACTIVE", "FINISHED", "CANCELED", "PANIC"})
    public void test_WithdrawRide_InvalidStatus(RideStatus status){
        setUpRide(status);
        Exception e = assertThrows(BadRequestException.class, ()-> rideService.withdrawRide(VALID_ID));
        assertEquals(WITHDRAW_ERROR, e.getMessage());
        verify(rideRepository, times(1)).findById(VALID_ID);
    }


    // START

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"ACCEPTED"})
    public void test_StartRide_ValidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        RideResponseDTO response = rideService.startRide(VALID_ID);
        assertEquals(VALID_ID, response.getId());
        assertEquals(RideStatus.ACTIVE, response.getStatus());
        verify(rideRepository, times(1)).findById(VALID_ID);
        verify(rideRepository, times(1)).save(ride);
    }

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"PENDING", "REJECTED", "ACTIVE", "FINISHED", "CANCELED", "PANIC"})
    public void test_StartRide_InvalidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        Exception e = assertThrows(BadRequestException.class, ()-> rideService.startRide(VALID_ID));
        assertEquals(START_ERROR, e.getMessage());
        verify(rideRepository, times(1)).findById(VALID_ID);

    }


    // ACCEPT

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"PENDING"})
    public void test_AcceptRide_ValidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        RideResponseDTO response = rideService.acceptRide(VALID_ID);
        assertEquals(VALID_ID, response.getId());
        assertEquals(RideStatus.ACCEPTED, response.getStatus());
        verify(rideRepository, times(1)).findById(VALID_ID);
        verify(rideRepository, times(1)).save(ride);
    }

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"ACCEPTED", "REJECTED" , "ACTIVE", "FINISHED", "CANCELED", "PANIC"})
    public void test_AcceptRide_InvalidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        Exception e = assertThrows(BadRequestException.class, ()-> rideService.acceptRide(VALID_ID));
        assertEquals(ACCEPT_ERROR, e.getMessage());
        verify(rideRepository, times(1)).findById(VALID_ID);


    }

    // END

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"ACTIVE"})
    public void test_EndRide_ValidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        RideResponseDTO response = rideService.endRide(VALID_ID);
        assertEquals(VALID_ID, response.getId());
        assertEquals(RideStatus.FINISHED, response.getStatus());
        verify(rideRepository, times(1)).findById(VALID_ID);
        verify(rideRepository, times(1)).save(ride);

    }
    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"PENDING","ACCEPTED", "REJECTED" , "FINISHED", "CANCELED", "PANIC"})
    public void test_EndRide_InvalidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        Exception e = assertThrows(BadRequestException.class, ()-> rideService.endRide(VALID_ID));
        assertEquals(END_ERROR, e.getMessage());
        verify(rideRepository, times(1)).findById(VALID_ID);

    }

    // CANCEL

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"PENDING", "ACCEPTED"})
    public void test_CancelRide_ValidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);

        Rejection rejection = new Rejection();
        rejection.setReason("Test");

        User user = mock(User.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(rejectionRepository.save(rejection)).thenReturn(rejection);

        RideResponseDTO responseDTO = rideService.cancelRide(VALID_ID, rejection);

        assertNotNull(responseDTO);
        assertEquals(RideStatus.REJECTED, responseDTO.getStatus());
        verify(rideRepository, times(1)).findById(VALID_ID);
        verify(rejectionRepository, times(1)).save(rejection);
        verify(rideRepository, times(1)).save(ride);
    }

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names = {"REJECTED", "ACTIVE", "FINISHED", "CANCELED", "PANIC"})
    public void test_CancelRide_InvalidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        Rejection rejection = new Rejection();
        rejection.setReason("Test");

        BadRequestException e = assertThrows(BadRequestException.class, () -> rideService.cancelRide(VALID_ID, rejection));
        assertEquals(CANCEL_ERROR, e.getMessage());

    }

    @ParameterizedTest
    @EnumSource(value=RideStatus.class, names = {"ACTIVE", "ACCEPTED"})
    public void test_SetPanic_ValidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        ride.setPanic(false);
        Ride result = rideService.setPanic(VALID_ID);

        assertTrue(result.isPanic());
        assertEquals(RideStatus.PANIC, result.getStatus());
        verify(rideRepository, times(1)).save(ride);
    }

    @ParameterizedTest
    @EnumSource(value = RideStatus.class, names={"PENDING", "REJECTED", "FINISHED", "CANCELED", "PANIC"})
    public void test_SetPanic_InvalidStatus(RideStatus rideStatus){
        setUpRide(rideStatus);
        ride.setPanic(false);
        BadRequestException e = assertThrows(BadRequestException.class, () -> rideService.setPanic(VALID_ID));
        assertEquals(PANIC_ERROR, e.getMessage());
    }

    @ParameterizedTest
    @EnumSource(value=RideStatus.class, names={"PENDING", "ACCEPTED", "REJECTED", "ACTIVE", "FINISHED", "CANCELED", "PANIC"})
    public void test_findRideByStatus_NotEmpty(RideStatus rideStatus){
        ride.setStatus(rideStatus);
        List<Ride> rides = Arrays.asList(ride);
        when(rideRepository.findByStatus(rideStatus)).thenReturn(rides);

        List<Ride> response = rideService.findByStatus(rideStatus);
        assertNotNull(response);
        assertEquals(rides, response);
        assertEquals(1, response.size());

    }

    @ParameterizedTest
    @EnumSource(value=RideStatus.class, names={"PENDING", "ACCEPTED", "REJECTED", "ACTIVE", "FINISHED", "CANCELED", "PANIC"})
    public void test_findRideByStatus_Empty(RideStatus rideStatus){
        ride.setStatus(rideStatus);
        List<Ride> rides = new ArrayList<>();
        when(rideRepository.findByStatus(rideStatus)).thenReturn(new ArrayList<>());

        List<Ride> response = rideService.findByStatus(rideStatus);
        assertNotNull(response);
        assertEquals(rides, response);
        assertEquals(0, response.size());

    }
    @Test
    public void test_CountRidesForDriver_Empty(){
        when(rideRepository.findAllByDriver(driver)).thenReturn(new ArrayList<>());
        int response = rideService.countForDriver(driver);
        assertEquals(0, response);
    }

    @Test
    public void test_CountRidesForDriver_NotEmpty(){
        List<Ride> rides = Arrays.asList(ride);
        when(rideRepository.findAllByDriver(driver)).thenReturn(rides);
        assertEquals(1 , rideService.countForDriver(driver) );
    }

    @Test
    public void test_CountRidesForPassenger_Empty(){
        when(rideRepository.findAllByPassengersContaining(passenger)).thenReturn(new ArrayList<>());
        int response = rideService.countForPsngr(passenger);
        assertEquals(0, response);
    }

    @Test
    public void test_CountRidesForPassenger_NotEmpty(){
        List<Ride> rides = Arrays.asList(ride);
        when(rideRepository.findAllByPassengersContaining(passenger)).thenReturn(rides);
        assertEquals(1 , rideService.countForPsngr(passenger));
    }

    @Test
    public void test_FilterRidesForNotification_NullScheduledTime(){
        List<Ride> rides = createTestRidesNullScheduledTime();
        int i = 15;
        List<Ride> response = rideService.filterRidesForNotification(rides, i);
        assertTrue(response.isEmpty());
    }

    @Test
    public void test_FilterRidesForNotification_Empty(){
        List<Ride> rides = new ArrayList<>();
        int i = 15;
        List<Ride> response = rideService.filterRidesForNotification(rides, i);
        assertTrue(response.isEmpty());
    }

    @Test
    public void test_FilterRidesForNotification_ValidRides(){
        List<Ride> rides = createTestRides();
        int i = 15;
        List<Ride> response = rideService.filterRidesForNotification(rides,i);
        assertEquals(1, response.size());
    }
    @Test
    public void test_FilterRidesForNotification_ValidRides_ZeroI(){
        List<Ride> rides = createTestRides();
        int i = 0;
        List<Ride> response = rideService.filterRidesForNotification(rides, i);
        assertTrue(response.isEmpty());
    }

    @Test
    public void test_FilterRidesForNotification_ValidRides_NegativeI(){
        List<Ride> rides = createTestRides();
        int i = -30;
        List<Ride> response = rideService.filterRidesForNotification(rides, i);
        assertEquals(0, response.size());
    }

    @Test
    public void test_AddPassengers_InvalidRide(){
        setUpRide(RideStatus.PENDING);
        RideDTO dto = new RideDTO();
        Exception e = assertThrows(EntityNotFoundException.class, () -> rideService.addPassengers(dto, INVALID_ID, VALID_ID));
        assertEquals("Ride does not exist", e.getMessage());
    }

    @Test
    public void test_AddPassengers_InvalidUser(){
        setUpRide(RideStatus.PENDING);
        setUpForPassenger();
        Exception e = assertThrows(EntityNotFoundException.class, () -> rideService.addPassengers(new RideDTO(), VALID_ID, INVALID_ID));
        assertEquals(PASSENGER_ERROR, e.getMessage());
    }

    @Test
    public void test_AddPassengers_NullList(){
        setUpRide(RideStatus.PENDING);
        setUpForPassenger();
        Ride response = rideService.addPassengers(new RideDTO(), VALID_ID, VALID_ID);
        assertNotNull(response);
        assertEquals(1, response.getPassengers().size());
        assertEquals(VALID_ID, response.getId());
    }

    @Test
    public void test_AddPassengers_EmptyList(){
        setUpRide(RideStatus.PENDING);
        setUpForPassenger();
        RideDTO dto = new RideDTO();
        dto.setPassengers(new ArrayList<>());
        Ride response = rideService.addPassengers(dto, VALID_ID, VALID_ID);
        assertNotNull(response);
        assertEquals(1, response.getPassengers().size());
        assertEquals(VALID_ID, response.getId());
    }

    @Test
    public void test_AddPassengers_InvalidEmail(){
        setUpForPassenger();
        setUpRide(RideStatus.PENDING);
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setEmail(INVALID_EMAIL);
        RideDTO dto = new RideDTO();
        dto.setPassengers(new ArrayList<>(Arrays.asList(userShortDTO)));

        Exception e = assertThrows(EntityNotFoundException.class, ()->rideService.addPassengers(dto, VALID_ID, VALID_ID));
        assertEquals(PASSENGER_ERROR, e.getMessage());
    }

    @Test
    public void test_AddPassengers_Valid(){
        setUpForPassenger();
        setUpRide(RideStatus.PENDING);
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setEmail(VALID_EMAIL);
        RideDTO dto = new RideDTO();
        dto.setPassengers(new ArrayList<>(Arrays.asList(userShortDTO)));
        Ride response = rideService.addPassengers(dto, VALID_ID, VALID_ID);
        assertEquals(2, response.getPassengers().size());

    }

    @Test
    public void test_AddRoutes_InvalidRide(){
        setUpRide(RideStatus.PENDING);
        Exception e = assertThrows(EntityNotFoundException.class, () -> rideService.addRoutes(new RideDTO(), INVALID_ID));
        assertEquals("Ride does not exist", e.getMessage());
    }

    @Test
    public void test_AddRoutes_ExistingRoutes(){
        setUpRide(RideStatus.PENDING);
        ride.setVehicleType(VehicleEnum.STANDARD);
        RideDTO rideDTO = new RideDTO();
        Route route = new Route();
        Location departure = new Location(11.11, 22.22, "Address");
        route.setDeparture(departure);
        Location destination = new Location(22.22, 11.11 ,"Address 2");
        route.setDestination(destination);

        Route existing = new Route();
        existing.setDeparture(departure);
        existing.setDestination(destination);
        existing.setDistance(5.5);

        when(routeService.findByLocationAddress(route.getDeparture().getAddress(), route.getDestination().getAddress())).thenReturn(existing);
        rideDTO.setLocations(new ArrayList<>(Arrays.asList(route)));

        VehicleType vehicleType = new VehicleType();
        vehicleType.setPrice(100);
        when(vehicleTypeRepository.findByType(VehicleEnum.STANDARD)).thenReturn(Optional.of(vehicleType));

        Ride result = rideService.addRoutes(rideDTO, VALID_ID);

        assertEquals(1, result.getLocations().size());
        assertEquals(760.0, result.getTotalCost());
        assertEquals(5.5, result.getTotalDistance());

    }

    @Test
    public void test_AddRoutes_NewRoutes(){
        setUpRide(RideStatus.PENDING);
        RideDTO dto = new RideDTO();
        Route route = new Route();
        ride.setVehicleType(VehicleEnum.STANDARD);
        route.setDeparture(new Location(11.11, 22.22, "Address"));
        route.setDestination(new Location(22.22, 11.11 ,"Address 2"));
        dto.setLocations(new ArrayList<>(Arrays.asList(route)));
        VehicleType vehicleType = new VehicleType();
        vehicleType.setPrice(100);
        when(vehicleTypeRepository.findByType(VehicleEnum.STANDARD)).thenReturn(Optional.of(vehicleType));

        when(routeService.findByLocationAddress(route.getDeparture().getAddress(), route.getDestination().getAddress())).thenReturn(null);
        when(rideSchedulingService.getEstimates(route.getDeparture(), route.getDestination())).thenReturn(Arrays.asList(330.0, 5.5));
        when(routeService.save(route)).thenReturn(route);

        Ride result = rideService.addRoutes(dto, VALID_ID);

        assertEquals(1, result.getLocations().size());
        assertEquals(6.0, result.getEstimatedTimeInMinutes());
        assertEquals(5.5, result.getTotalDistance());
        assertEquals(760.0, result.getTotalCost());
    }

    @Test
    public void test_AddRoutes_InvalidVehicleType(){
        setUpRide(RideStatus.PENDING);
        RideDTO dto = new RideDTO();
        Route route = new Route();
        ride.setVehicleType(VehicleEnum.STANDARD);
        route.setDeparture(new Location(11.11, 22.22, "Address"));
        route.setDestination(new Location(22.22, 11.11 ,"Address 2"));
        dto.setLocations(new ArrayList<>(Arrays.asList(route)));
        when(vehicleTypeRepository.findByType(VehicleEnum.VAN)).thenThrow(new EntityNotFoundException("Vehicle type does not exist!"));
        when(routeService.findByLocationAddress(route.getDeparture().getAddress(), route.getDestination().getAddress())).thenReturn(null);
        when(rideSchedulingService.getEstimates(route.getDeparture(), route.getDestination())).thenReturn(Arrays.asList(330.0, 5.5));
        when(routeService.save(route)).thenReturn(route);

        Exception e = assertThrows(EntityNotFoundException.class, () -> rideService.addRoutes(dto, VALID_ID));
        assertEquals("Vehicle type does not exist!", e.getMessage());
    }

    @Test
    public void test_AddDriver(){
        ride.setId(VALID_ID);
        driver.setId(VALID_ID);
        driver.setVehicle(new Vehicle());
        when(rideRepository.save(ride)).thenReturn(ride);

        Ride response = rideService.addDriver(ride, driver);

        assertEquals(VALID_ID, response.getDriver().getId());
        assertEquals(VALID_ID, response.getId());
        assertEquals(driver.getVehicle(), response.getVehicle());
        verify(rideRepository, times(1)).save(ride);

    }

    @Test
    public void test_SavedRide_ScheduledTime(){
        RideDTO dto = new RideDTO();
        LocalDateTime schedule = LocalDateTime.now();
        dto.setScheduledTime(schedule);
        ride.setScheduledTime(schedule);

        when(rideRepository.save(Mockito.any(Ride.class))).thenReturn(ride);

        Ride result = rideService.save(dto);

        assertNotNull(result);
        assertEquals(schedule, result.getScheduledTime());
    }

    @Test
    public void test_SaveRide(){
        RideDTO dto = new RideDTO();
        dto.setEstimatedTime(7);
        dto.setVehicleType(VehicleEnum.STANDARD);
        dto.setBabyTransport(false);
        dto.setPetTransport(true);

        ride.setEstimatedTimeInMinutes(7);
        ride.setVehicleType(VehicleEnum.STANDARD);
        ride.setBabyTransport(false);
        ride.setPetTransport(true);

        when(rideRepository.save(Mockito.any(Ride.class))).thenReturn(ride);

        Ride result = rideService.save(dto);

        assertNotNull(result);
        assertEquals(dto.getEstimatedTime(), result.getEstimatedTimeInMinutes());
        assertEquals(dto.getVehicleType(), result.getVehicleType());
        assertTrue(ride.isPetTransport());
        assertFalse(ride.isBabyTransport());
    }

    @Test
    public void test_FilterRidesForScheduling_EmptyList(){
        List<Ride> response = rideService.filterRidesForScheduling(new ArrayList<>());
        assertEquals(0, response.size());
    }

    @Test
    public void test_FilterRidesForScheduling_InvalidStatus(){
        List<Ride> rides = createTestRides();
        for(Ride r : rides){
            r.setStatus(RideStatus.ACCEPTED);
        }
        List<Ride> response = rideService.filterRidesForScheduling(rides);
        assertEquals(0, response.size());
    }

    @Test
    public void test_FilterRidesForScheduling_NullSchedule(){
        List<Ride> rides = createTestRidesNullScheduledTime();
        assertEquals(0, rideService.filterRidesForScheduling(rides).size());
    }

    @Test
    public void test_FilterRidesForScheduling_ValidValues(){
        List<Ride> rides = createTestRides();
        assertEquals(1, rideService.filterRidesForScheduling(rides).size());
    }

    @Test
    public void test_findPotentialDriver_Success(){
        when(rideSchedulingService.findDriver(any(Ride.class))).thenReturn(driver);
        assertEquals(driver, rideService.findPotentialDriver(ride));
    }

    @Test
    public void test_findPotentialDriver_Null(){
        when(rideSchedulingService.findDriver(any(Ride.class))).thenReturn(null);
        assertNull(rideService.findPotentialDriver(ride));
    }

    private List<Ride> createTestRidesNullScheduledTime(){
        List<Ride> rides = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            Ride r = new Ride();
            r.setStatus(RideStatus.PENDING);
            rides.add(r);
        }
        return rides;
    }
    private List<Ride> createTestRides(){
        Ride ride1 = new Ride();
        ride1.setScheduledTime(LocalDateTime.now().plusMinutes(14));
        ride1.setStatus(RideStatus.PENDING);
        Ride ride2 = new Ride();
        ride2.setScheduledTime(LocalDateTime.now().plusMinutes(30));
        ride2.setStatus(RideStatus.PENDING);
        Ride ride3 = new Ride();
        ride3.setScheduledTime(LocalDateTime.now().minusMinutes(15));
        ride3.setStatus(RideStatus.PENDING);
        return Arrays.asList(ride1, ride2 ,ride3);
    }

    private void setUpRide(RideStatus status){
        Mockito.when(rideRepository.findById(VALID_ID)).thenReturn(Optional.of(ride));
        Mockito.when(rideRepository.save(Mockito.any(Ride.class))).thenReturn(ride);
        ride.setStatus(status);
        ride.setId(VALID_ID);
    }

    private void setUpForDriver(){
        driver.setId(VALID_ID);
        ride.setId(VALID_ID);
        Mockito.when(driverService.findById(VALID_ID)).thenReturn(driver);
        Mockito.when(driverService.findById(INVALID_ID)).thenThrow(new EntityNotFoundException(DRIVER_ERROR));
        Mockito.when(rideRepository.findByDriverAndStatus(driver, RideStatus.ACTIVE)).thenReturn(Optional.of(ride));
    }

    private void setUpForPassenger(){
        Mockito.when(passengerService.findOne(VALID_ID)).thenReturn(passenger);
        Mockito.when(passengerService.findOne(INVALID_ID)).thenThrow(new EntityNotFoundException(PASSENGER_ERROR));
        Mockito.when(passengerService.findByEmail(VALID_EMAIL)).thenReturn(passenger);
        Mockito.when(passengerService.findByEmail(INVALID_EMAIL)).thenThrow(new EntityNotFoundException(PASSENGER_ERROR));
        List<Ride> li = new ArrayList<>();
        li.add(ride);
        Mockito.when(rideRepository.findByPassengersContainingAndStatus(passenger, RideStatus.ACTIVE)).thenReturn(li);

    }

}
