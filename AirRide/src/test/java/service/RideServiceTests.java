package service;

import com.uber.app.team23.AirRide.AirRideApplication;
import com.uber.app.team23.AirRide.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = AirRideApplication.class)
public class RideServiceTests {
    @Autowired
    private RideService rideService;
    @MockBean
    private PanicService panicService;
    @MockBean
    private FavoriteService favoriteService;
    @MockBean
    private UserService userService;
    @MockBean
    private RideSchedulingService rideSchedulingService;



}
