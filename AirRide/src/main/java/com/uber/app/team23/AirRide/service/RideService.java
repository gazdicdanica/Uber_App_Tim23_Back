package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.controller.WebSocketController;
import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.RideDTOMapper;
import com.uber.app.team23.AirRide.model.messageData.Rejection;
import com.uber.app.team23.AirRide.model.rideData.Ride;
import com.uber.app.team23.AirRide.model.rideData.RideStatus;
import com.uber.app.team23.AirRide.model.rideData.Route;
import com.uber.app.team23.AirRide.model.users.Passenger;
import com.uber.app.team23.AirRide.model.users.User;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleEnum;
import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.VehicleType;
import com.uber.app.team23.AirRide.repository.RejectionRepository;
import com.uber.app.team23.AirRide.repository.RideRepository;
import com.uber.app.team23.AirRide.repository.VehicleTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;


@Transactional
@Service
public class RideService {

    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private PassengerService passengerService;
    @Autowired
    private RouteService routeService;
    @Autowired
    private RideSchedulingService rideSchedulingService;
    @Autowired
    private VehicleTypeRepository vehicleTypeRepository;
    @Autowired
    private RejectionRepository rejectionRepository;
    @Autowired
    WebSocketController webSocketController;

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void scheduledRides() {
        System.err.println("Usao u scheduled");
        List<Ride> rides = rideRepository.findAll();
        rides = filterRidesForScheduling(rides);
        for (Ride ride : rides) {
            Driver driver = findPotentialDriver(ride);
            ride = addDriver(ride, driver);
            RideResponseDTO dto = new RideResponseDTO(ride);
            webSocketController.simpMessagingTemplate.convertAndSend("/ride-driver/" + driver.getId(), dto);
        }
    }

    private List<Ride> filterRidesForScheduling(List<Ride> rides) {
        List<Ride> schedule = new ArrayList<>();
        for (Ride ride : rides) {
            if (ride.getStatus() == RideStatus.PENDING) {
                if (ride.getScheduledTime() != null) {
                    if (ride.getScheduledTime().isAfter(LocalDateTime.now()) &&
                            ride.getScheduledTime().isBefore(LocalDateTime.now().plusMinutes(15))) {
                        schedule.add(ride);
                    }
                }
            }
        }
        return schedule;
    }

    public Ride findOne(Long id){
        return rideRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ride does not exist"));
    }

    public RideResponseDTO findActiveByDriver(Long driverId){
        return rideRepository.findActiveByDriver(driverId).orElse(null);
    }

    public RideResponseDTO findActiveByPassenger(Long passengerId){
        return rideRepository.findActiveByPassenger(passengerId).orElse(null);
    }

    public Ride addPassengers(RideDTO rideDTO, Long rideId, Long userId){
        Ride ride = this.findOne(rideId);
        Passenger creator = passengerService.findOne(userId);
        ride.addPassenger(creator);
        for(UserShortDTO user: rideDTO.getPassengers()){
            Passenger p = passengerService.findByEmail(user.getEmail());
            ride.getPassengers().add(p);
        }
        return rideRepository.save(ride);
    }

    public Ride addRoutes(RideDTO rideDTO, Long id){
        Ride ride = this.findOne(id);
        ride.setLocations(new ArrayList<>());
        int estimatedTime = 0;
        double distance = 0;
        for(Route route: rideDTO.getLocations()){
            Route r = routeService.findByLocationAddress(route.getDeparture().getAddress(), route.getDestination().getAddress());
            if(r == null){
                r = routeService.save(route);
            }
            ride.getLocations().add(r);
            List<Double> estimation = rideSchedulingService.getEstimates(r.getDeparture(), r.getDestination());
            int time =(int) Math.round(estimation.get(0)/60);
            estimatedTime += time;
            System.err.println("DISTANCE BEFORE CHANGE" + r.getDistance());
            if (r.getDistance() == 0){
                System.err.println("it is 0");
                System.err.println("estimation " + estimation.get(1));
                r.setDistance(estimation.get(1));
                distance += estimation.get(1);
                routeService.save(r);
            }else{
                distance += r.getDistance();
            }
        }
        VehicleEnum vehicleEnum = ride.getVehicleType();
        VehicleType vehicleType = vehicleTypeRepository.findByType(vehicleEnum).orElse(null);
        double price = (double) Math.round((distance*120 + vehicleType.getPrice())*100) / 100;
        ride.setTotalCost(price);
        double dist = (double)Math.round(distance * 100) /100;
        ride.setTotalDistance(dist);
        ride.setEstimatedTimeInMinutes(estimatedTime);
        return rideRepository.save(ride);
    }

    public void checkPassengerRide(Long passengerId){
        Ride ride = rideRepository.findPendingByPassenger(passengerId).orElse(null);
        Ride accepted = rideRepository.findAcceptedByPassenger(passengerId).orElse(null);
        if(ride != null || accepted != null){
            throw new BadRequestException("Cannot create a ride while you have one already pending!");
        }
    }

    public Driver findPotentialDriver(Ride ride){
        // TODO send notification to driver
        return this.rideSchedulingService.findDriver(ride);
    }

    public Ride save(RideDTO rideDTO){
        for(UserShortDTO u : rideDTO.getPassengers()){
            this.checkPassengerRide((long)u.getId());
        }
        Ride ride = new Ride();
        // potential start of ride
        if(rideDTO.getScheduledTime() != null){
            ride.setStartTime(rideDTO.getScheduledTime());
            ride.setScheduledTime(rideDTO.getScheduledTime());
        }else{
            ride.setStartTime(LocalDateTime.now());
        }
        ride.setStatus(RideStatus.PENDING);
        ride.setPanic(false);
        ride.setEstimatedTimeInMinutes((int) rideDTO.getEstimatedTime());
        ride.setVehicleType(rideDTO.getVehicleType());
        ride.setBabyTransport(rideDTO.isBabyTransport());
        ride.setPetTransport(rideDTO.isPetTransport());
        return rideRepository.save(ride);
    }

    public Ride addDriver(Ride ride, Driver driver){
        ride.setDriver(driver);
        ride.setVehicle(driver.getVehicle());
        return rideRepository.save(ride);
    }

    

    public RideResponseDTO withdrawRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getStatus() != RideStatus.ACCEPTED && ride.getStatus() != RideStatus.PENDING){
            throw new BadRequestException("Cannot cancel a ride that is not in status PENDING or ACCEPTED");
        }
        ride.setStatus(RideStatus.CANCELED);

        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public RideResponseDTO startRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getStatus() == RideStatus.ACCEPTED){
            ride.setStatus(RideStatus.ACTIVE);
            ride.setStartTime(LocalDateTime.now());
            return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
        }
        throw new BadRequestException("Cannot start a ride that is not in status ACCEPTED!");
    }

    public RideResponseDTO acceptRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getStatus() != RideStatus.PENDING){
            throw new BadRequestException("Cannot accept a ride that is not in status PENDING!");
        }
        ride.setStatus(RideStatus.ACCEPTED);
        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public RideResponseDTO endRide(Long id){
        Ride ride = this.findOne(id);
        if(ride.getStatus() != RideStatus.ACTIVE){
            throw new BadRequestException("Cannot end a ride that is not in status ACTIVE!");
        }
        ride.setStatus(RideStatus.FINISHED);
        ride.setEndTime(LocalDateTime.now());
        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public RideResponseDTO cancelRide(Long id, Rejection rejection){
        Ride ride = this.findOne(id);
        if(ride.getStatus() != RideStatus.PENDING && ride.getStatus() != RideStatus.ACCEPTED){
            throw new BadRequestException("Cannot cancel a ride that is not in status PENDING or ACCEPTED!");
        }
        // Rejection repository?
        Rejection r = new Rejection();
        ride.setStatus(RideStatus.REJECTED);
        r.setRide(ride);
        r.setTimeOfRejection(LocalDateTime.now());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        r.setUser(user);
        r.setReason(rejection.getReason());
        r = rejectionRepository.save(rejection);
        ride.setRejection(r);
        return RideDTOMapper.fromRideToDTO(rideRepository.save(ride));
    }

    public Ride setPanic(Long id){
        Ride ride = this.findOne(id);
        ride.setPanic(true);
        ride.setStatus(RideStatus.PANIC);
        ride = rideRepository.save(ride);
        return ride;
    }

    public Page<Ride> findAllByDriver(Driver byId, Pageable pageable) {
        return rideRepository.findAllByDriver(byId, pageable);
    }

    public List<Ride> findByStatus(RideStatus status){
        return rideRepository.findByStatus(status);
    }

    public int countForDriver(User user) {
        List<Ride> list = rideRepository.findAllByDriver(user);
        return  list.size();
    }

    public int countForPsngr(User u) {
        List<Ride> list = rideRepository.findAllByPassengersContaining(u);
        return  list.size();
    }

//    public Page<Ride> findAllByPassenger(Passenger passenger, Pageable pageable){
//        return rideRepository.findAllByPassengers(passenger, pageable);
//    }
}
