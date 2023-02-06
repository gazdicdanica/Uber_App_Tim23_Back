package controller;

import com.uber.app.team23.AirRide.AirRideApplication;
import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.*;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = AirRideApplication.class)
public class RideControllerTests {
    private static final String EMAIL_USER="test@email.com";
    private static final String PASSWORD_USER="123";
    private static final int ID_USER = 2;

    private static final String EMAIL_DRIVER="pp@gmail.com";
    private static final String PASSWORD_DRIVER="123";
    private static final int ID_DRIVER = 4;
    @Autowired
    private TestRestTemplate restTemplate;

    private String driverToken;
    private String passengerToken;
    private ArrayList<Route> locations = new ArrayList<>();
    private ArrayList<UserShortDTO> passengers = new ArrayList<>();
    private HttpHeaders headers = new HttpHeaders();
    private RideDTO requestRide;
    private boolean favoriteSet = false, testFavNotSet = false, testFavNotDel = false;
    private FavoriteDTO favoriteDTO;
    private Long favoriteId;

    @BeforeAll
    public void login() {
        ResponseEntity<TokensDTO> driverLogin = restTemplate.postForEntity( "/api/user/login",
                new LoginDTO(EMAIL_DRIVER, PASSWORD_DRIVER), TokensDTO.class);

        driverToken = driverLogin.getBody().getAccessToken();

        ResponseEntity<TokensDTO> passengerLogin = restTemplate.postForEntity( "/api/user/login",
                new LoginDTO(EMAIL_USER, PASSWORD_USER), TokensDTO.class);

        passengerToken = passengerLogin.getBody().getAccessToken();

        this.locations.add(new Route(new Location(45.25869, 19.83579, "Dimitrija Avramovica 3"),
                new Location(45.25224, 19.85456, "Maksima Gorkog 57"), 2.2));

        // Start Driver Shift
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<?> whEntity = new HttpEntity<>(headers);
        ResponseEntity<WorkHoursDTO> whResponse = restTemplate.exchange(
                "/api/driver/"+ID_DRIVER+"/working-hour/start",
                HttpMethod.PUT,
                whEntity,
                WorkHoursDTO.class);

        favoriteDTO = new FavoriteDTO("Omiljena 1", locations, passengers, false, false, VehicleEnum.STANDARD);

        assertEquals(HttpStatus.OK, whResponse.getStatusCode());

    }

    @BeforeEach
    public void setData() {
        requestRide = new RideDTO(this.locations, this.passengers, VehicleEnum.STANDARD, false, false, null);
    }


    @Test
    @DisplayName("Happy Flow For Entire Ride Process")
    public void entireRideProcessHappyFlow() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        // Arrived At Destination And Ending Ride
        HttpEntity<Long> endRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> finishedRide = restTemplate.exchange(
                "/api/ride/" + rideId + "/end",
                HttpMethod.PUT,
                endRide,
                RideResponseDTO.class);

        assertEquals(HttpStatus.OK, finishedRide.getStatusCode());
        assertEquals(RideStatus.FINISHED, finishedRide.getBody().getStatus());

    }

    @Test
    @DisplayName("Trying to start new ride when all drivers are busy")
    public void createRideNoAvailableDriver() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Create Second Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity2 = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response2 = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity2,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse2 = response2.getBody();
        assertEquals(null, rideResponse2.getId());

        // Driver Cancels Ride With Explanation For Him To Be Available
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        Rejection rejection = new Rejection("Flat Tire");
        HttpEntity<Rejection> rejectionEntity = new HttpEntity<>(rejection, headers);
        ResponseEntity<RideResponseDTO> rejectionResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/cancel",
                HttpMethod.PUT,
                rejectionEntity,
                RideResponseDTO.class);

    }

    @Test
    @DisplayName("Ride Panicked After Starting")
    public void ridePanicked() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getLocations().size());
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        //Passenger Presses Panic Button
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        PanicDTO panicDTO = panicResponse.getBody();
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(panicDTO.getReason(), panic.getReason());
    }

    @Test
    @DisplayName("Non Existing Ride Panicked")
    public void notExistingRidePanicked() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getLocations().size());
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        // Panicking NonExisting Ride
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + 1234 + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        assertEquals(0, panicResponse.getBody().getId());

        // Panicking Previous Ride
        ResponseEntity<PanicDTO> pr = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
    }

    @Test
    @DisplayName("Passenger Ride Withdrawn ")
    public void rideWithdrawn() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Passenger Withdraws Ride
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> withdrawEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> withdrawResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/withdraw",
                HttpMethod.PUT,
                withdrawEntity,
                RideResponseDTO.class);
        assertEquals(RideStatus.CANCELED, withdrawResponse.getBody().getStatus());
    }

    @Test
    @DisplayName("Passenger NonExistingRide Withdrawn ")
    public void nonExistingRideWithdrawn() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Passenger Withdraws Ride
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> withdrawEntityInvalid = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> withdrawResponseInvalid = restTemplate.exchange(
                "/api/ride/" + 1234 + "/withdraw",
                HttpMethod.PUT,
                withdrawEntityInvalid,
                RideResponseDTO.class);
        assertEquals(null, withdrawResponseInvalid.getBody().getId());

        // Passenger Withdraws Ride
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> withdrawEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> withdrawResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/withdraw",
                HttpMethod.PUT,
                withdrawEntity,
                RideResponseDTO.class);
    }

    @Test
    @DisplayName("Driver Ride Canceled")
    public void rideCanceled() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Cancels Ride With Explanation
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        Rejection rejection = new Rejection("Flat Tire");
        HttpEntity<Rejection> rejectionEntity = new HttpEntity<>(rejection, headers);
        ResponseEntity<RideResponseDTO> rejectionResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/cancel",
                HttpMethod.PUT,
                rejectionEntity,
                RideResponseDTO.class);
        assertEquals(RideStatus.REJECTED, rejectionResponse.getBody().getStatus());
    }

    @Test
    @DisplayName("Driver NonExistingRide Canceled")
    public void nonExistingRideCanceled() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Cancels Invalid Ride With Explanation
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        Rejection rejection = new Rejection("Flat Tire");
        HttpEntity<Rejection> rejectionEntityInvalid = new HttpEntity<>(rejection, headers);
        ResponseEntity<RideResponseDTO> rejectionResponseInvalid = restTemplate.exchange(
                "/api/ride/" + 123 + "/cancel",
                HttpMethod.PUT,
                rejectionEntityInvalid,
                RideResponseDTO.class);
        assertEquals(null, rejectionResponseInvalid.getBody().getStatus());

        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        PanicDTO panicDTO = panicResponse.getBody();
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(panicDTO.getReason(), panic.getReason());
    }

    @Test
    @DisplayName("Get Detail For Specific EXISTING Ride")
    public void getRideDetails() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Get Ride Details As Driver
        HttpEntity<Long> getDetailsEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> detailsResponse = restTemplate.exchange(
                "/api/ride/" + rideId,
                HttpMethod.GET,
                getDetailsEntity,
                RideResponseDTO.class);
        RideResponseDTO responseDTO = detailsResponse.getBody();
        assertEquals(rideId, responseDTO.getId());
        assertEquals(1, responseDTO.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, responseDTO.getVehicleType());
        assertEquals(RideStatus.PENDING, responseDTO.getStatus());
        assertNotNull(responseDTO.getDriver());

        // Driver Cancels Ride With Explanation For Him To Be Available
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        Rejection rejection = new Rejection("Flat Tire");
        HttpEntity<Rejection> rejectionEntity = new HttpEntity<>(rejection, headers);
        ResponseEntity<RideResponseDTO> rejectionResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/cancel",
                HttpMethod.PUT,
                rejectionEntity,
                RideResponseDTO.class);
    }

    @Test
    @DisplayName("Get Detail For NON-EXISTING Ride")
    public void getRideDetailsNonExistingRide() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Get Ride Details As Driver
        HttpEntity<Long> getDetailsEntity = new HttpEntity<>(headers);
        ResponseEntity<Error> detailsResponse = restTemplate.exchange(
                "/api/ride/" + 12345,
                HttpMethod.GET,
                getDetailsEntity,
                Error.class);
        assertEquals("Ride does not exist" ,detailsResponse.getBody().getMessage());

        // Driver Cancels Ride With Explanation For Him To Be Available
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        Rejection rejection = new Rejection("Flat Tire");
        HttpEntity<Rejection> rejectionEntity = new HttpEntity<>(rejection, headers);
        ResponseEntity<RideResponseDTO> rejectionResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/cancel",
                HttpMethod.PUT,
                rejectionEntity,
                RideResponseDTO.class);
    }

    @Test
    @DisplayName("Get Detail For No ID Ride")
    public void getRideDetailsNoId() {

        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Get Ride Details As Driver
        HttpEntity<Long> getDetailsEntity = new HttpEntity<>(headers);
        ResponseEntity<Error> detailsResponse = restTemplate.exchange(
                "/api/ride/",
                HttpMethod.GET,
                getDetailsEntity,
                Error.class);
        assertEquals(null ,detailsResponse.getBody().getMessage());

        // Driver Cancels Ride With Explanation For Him To Be Available
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        Rejection rejection = new Rejection("Flat Tire");
        HttpEntity<Rejection> rejectionEntity = new HttpEntity<>(rejection, headers);
        ResponseEntity<RideResponseDTO> rejectionResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/cancel",
                HttpMethod.PUT,
                rejectionEntity,
                RideResponseDTO.class);
    }

    @Test
    @DisplayName("Get Active Ride For Driver")
    public void getRidesDriverActive() {
        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        // Getting Active Ride For Driver
        HttpEntity<Long> activeRideEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> activeRide = restTemplate.exchange(
                "/api/ride/driver/" + ID_DRIVER + "/active",
                HttpMethod.GET,
                activeRideEntity,
                RideResponseDTO.class);
        RideResponseDTO resp = activeRide.getBody();
        assertNotNull(resp);
        assertEquals(rideId, resp.getId());
        assertEquals(RideStatus.ACTIVE, resp.getStatus());

        // Driver Panics
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        PanicDTO panicDTO = panicResponse.getBody();
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(panicDTO.getReason(), panic.getReason());

    }

    @Test
    @DisplayName("Get Active Ride When No Active")
    public void getRidesDriverActiveWhenNonActive() {

        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);

        // Getting Active Ride For Driver
        HttpEntity<Long> activeRideEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> activeRide = restTemplate.exchange(
                "/api/ride/driver/" + ID_DRIVER + "/active",
                HttpMethod.GET,
                activeRideEntity,
                RideResponseDTO.class);
        RideResponseDTO resp = activeRide.getBody();
        assertNull(resp.getId());
    }

    @Test
    @DisplayName("Bad Input For Get Active Ride For Driver")
    public void getRidesDriverActiveBadInput() {
        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        // Getting Active Ride For Driver
        HttpEntity<Long> activeRideEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> activeRide = restTemplate.exchange(
                "/api/ride/driver/" + " " + "/active",
                HttpMethod.GET,
                activeRideEntity,
                RideResponseDTO.class);
        assertNull(activeRide.getBody().getId());

        //Panics
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        PanicDTO panicDTO = panicResponse.getBody();
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(panicDTO.getReason(), panic.getReason());

    }

    @Test
    @DisplayName("Get Active Ride For Passenger")
    public void getActiveRidesPassenger() {
        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        // Get Active Ride For Passenger
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> activeRideEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> activeRide = restTemplate.exchange(
                "/api/ride/passenger/" + 2 + "/active",
                HttpMethod.GET,
                activeRideEntity,
                RideResponseDTO.class);
        assertEquals(rideId, activeRide.getBody().getId());

        // Panics
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        PanicDTO panicDTO = panicResponse.getBody();
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(panicDTO.getReason(), panic.getReason());
    }

    @Test
    @DisplayName("Get Active Ride When No Active")
    public void getActiveRidePassengerWhenNonActive() {
        // Get Active Ride For Passenger
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> activeRideEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> activeRide = restTemplate.exchange(
                "/api/ride/passenger/" + 1L + "/active",
                HttpMethod.GET,
                activeRideEntity,
                RideResponseDTO.class);

        assertNull(activeRide.getBody().getId());
    }

    @Test
    @DisplayName("Get Active Ride For Bad Input")
    public void getActiveRidePassengerBadInput() {
        // Create Instant Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<RideDTO> entity = new HttpEntity<>(requestRide, headers);
        ResponseEntity<RideResponseDTO> response = restTemplate.exchange(
                "/api/ride",
                HttpMethod.POST,
                entity,
                RideResponseDTO.class);
        assertNotNull(response.getBody());
        RideResponseDTO rideResponse = response.getBody();
        Long rideId = rideResponse.getId();
        assertEquals(1, rideResponse.getPassengers().size());
        assertEquals(VehicleEnum.STANDARD, rideResponse.getVehicleType());
        assertEquals(RideStatus.PENDING, rideResponse.getStatus());
        assertNotNull(rideResponse.getDriver());

        // Driver Accepts Ride Request
        headers.clear();
        headers.add("authorization", "Bearer " + driverToken);
        HttpEntity<Long> acceptRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> driverRideResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/accept",
                HttpMethod.PUT,
                acceptRide,
                RideResponseDTO.class);

        assertNotNull(driverRideResponse.getBody());
        assertEquals(HttpStatus.OK, driverRideResponse.getStatusCode());

        // Driver Picked Up Passenger And Ride Started
        HttpEntity<RideResponseDTO> startRide = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> startedRideDTO = restTemplate.exchange(
                "/api/ride/" + rideId + "/start",
                HttpMethod.PUT,
                startRide,
                RideResponseDTO.class);
        assertEquals(HttpStatus.OK, startedRideDTO.getStatusCode());

        // Get Active Ride For Passenger
        headers.clear();
        headers.add("authorization", "Baerer" + passengerToken);
        HttpEntity<Long> activeRideEntity = new HttpEntity<>(headers);
        ResponseEntity<RideResponseDTO> activeRide = restTemplate.exchange(
                "/api/ride/passenger/" + " " + "/active",
                HttpMethod.GET,
                activeRideEntity,
                RideResponseDTO.class);

        assertNull(activeRide.getBody().getId());

        // Panics
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        Panic panic = new Panic(new Ride(), "Driving Too Fast");
        HttpEntity<Panic> panicEntity = new HttpEntity<>(panic, headers);
        ResponseEntity<PanicDTO> panicResponse = restTemplate.exchange(
                "/api/ride/" + rideId + "/panic",
                HttpMethod.PUT,
                panicEntity,
                PanicDTO.class);
        PanicDTO panicDTO = panicResponse.getBody();
        assertEquals(HttpStatus.OK, panicResponse.getStatusCode());
        assertEquals(panicDTO.getReason(), panic.getReason());
    }

    @Test
    @DisplayName("Set Favorite Route")
    public void postFavorites() {
        headers.clear();
        headers.set("authorization", "Bearer " + passengerToken);
        HttpEntity<FavoriteDTO> setFav = new HttpEntity<>(favoriteDTO, headers);
        ResponseEntity<FavoriteDTO> favResponse = restTemplate.exchange(
                "/api/ride/favorites",
                HttpMethod.POST,
                setFav,
                FavoriteDTO.class);
        assertEquals(favoriteDTO.getFavoriteName(), favResponse.getBody().getFavoriteName());
        favoriteDTO = favResponse.getBody();
        favoriteId = favResponse.getBody().getId();
        favoriteSet = true;
    }

    @Test
    @DisplayName("Set Favorite Route Bad Input")
    public void postFavoritesBadInput() {
        FavoriteDTO favoriteDTO = new FavoriteDTO(null, locations, passengers, false, false, VehicleEnum.STANDARD);
        headers.clear();
        headers.set("authorization", "Bearer " + passengerToken);
        HttpEntity<FavoriteDTO> setFav = new HttpEntity<>(favoriteDTO, headers);
        ResponseEntity<FavoriteDTO> favResponse = restTemplate.exchange(
                "/api/ride/favorites",
                HttpMethod.POST,
                setFav,
                FavoriteDTO.class);
        assertNull(favResponse.getBody().getId());
    }

    @Test
    @DisplayName("Get Favorite Routes")
    public void getFavorites() {
        if (!testFavNotSet) {
            return;
        }
        if (!favoriteSet) {
            return;
        }
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<?> getFav = new HttpEntity<>(headers);
        ResponseEntity<List<FavoriteDTO>> getFavResp = restTemplate.exchange(
                "/api/ride/favorites",
                HttpMethod.GET,
                getFav,
                new ParameterizedTypeReference<List<FavoriteDTO>>(){});
        System.err.println(getFavResp.getBody());
        assertTrue(getFavResp.getBody().contains(favoriteDTO));
    }

    @Test
    @DisplayName("Get Favorite Not Set")
    public void getFavoritesWhenNoFavoritesSet() {
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<?> getFav = new HttpEntity<>(headers);
        ResponseEntity<List<FavoriteDTO>> getFavResp = restTemplate.exchange(
                "/api/ride/favorites",
                HttpMethod.GET,
                getFav,
                new ParameterizedTypeReference<List<FavoriteDTO>>(){});
        assertFalse(getFavResp.getBody().contains(favoriteDTO));
        testFavNotSet = true;
    }

    @Test
    @DisplayName("Delete Favorite Route")
    public void deleteFavorites() {
        if (!favoriteSet) {
            return;
        }
        if (!testFavNotDel) {
            return;
        }
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> getFav = new HttpEntity<>(favoriteId, headers);
        ResponseEntity<Void> getFavResp = restTemplate.exchange(
                "/api/ride/favorites/" + favoriteId,
                HttpMethod.DELETE,
                getFav,
                Void.class);

        // Set Favorite Again
        headers.clear();
        headers.set("authorization", "Bearer " + passengerToken);
        HttpEntity<FavoriteDTO> setFav = new HttpEntity<>(favoriteDTO, headers);
        ResponseEntity<FavoriteDTO> favResponse = restTemplate.exchange(
                "/api/ride/favorites",
                HttpMethod.POST,
                setFav,
                FavoriteDTO.class);
        favoriteDTO = favResponse.getBody();
        favoriteId = favResponse.getBody().getId();


        assertEquals(HttpStatus.NO_CONTENT, getFavResp.getStatusCode());
    }

    @Test
    @DisplayName("Delete Route For Invalid Input")
    public void deleteFavoritesBadId() {
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> getFav = new HttpEntity<>(favoriteId, headers);
        ResponseEntity<Void> getFavResp = restTemplate.exchange(
                "/api/ride/favorites/",
                HttpMethod.DELETE,
                getFav,
                Void.class);
        assertNull(getFavResp.getBody());
    }

    @Test
    @DisplayName("Delete Non Existing Favorite Route")
    public void deleteFavoritesWhenNoFavoritesSet() {
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> getFav = new HttpEntity<>(favoriteId, headers);
        ResponseEntity<Void> getFavResp = restTemplate.exchange(
                "/api/ride/favorites/" + favoriteId,
                HttpMethod.DELETE,
                getFav,
                Void.class);
        assertNull(getFavResp.getBody());
        testFavNotDel = true;
    }

    @Test
    @DisplayName("Delete FavRide That Is Not Linked To You ")
    public void deleteFavoriteRideThatIsNotYoursShouldFail() {
        if(!favoriteSet) {
            return;
        }
        if (!testFavNotDel) {
            return;
        }
        headers.clear();
        headers.add("authorization", "Bearer " + passengerToken);
        HttpEntity<Long> getFav = new HttpEntity<>(favoriteId, headers);
        ResponseEntity<Void> getFavResp = restTemplate.exchange(
                "/api/ride/favorites/" + favoriteId,
                HttpMethod.DELETE,
                getFav,
                Void.class);
        System.err.println(getFavResp.getBody());
        assertNull(getFavResp.getBody());

        // Add Another Favorite For Other Tests To Success
        headers.clear();
        headers.set("authorization", "Bearer " + passengerToken);
        HttpEntity<FavoriteDTO> setFav = new HttpEntity<>(favoriteDTO, headers);
        ResponseEntity<FavoriteDTO> favResponse = restTemplate.exchange(
                "/api/ride/favorites",
                HttpMethod.POST,
                setFav,
                FavoriteDTO.class);
        favoriteDTO = favResponse.getBody();
        favoriteId = favResponse.getBody().getId();
    }
}
