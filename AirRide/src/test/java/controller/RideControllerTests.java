package controller;

import com.uber.app.team23.AirRide.AirRideApplication;
import com.uber.app.team23.AirRide.dto.*;
import com.uber.app.team23.AirRide.model.messageData.Panic;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Location;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;

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
        assertNotNull(response.getBody());
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
}
