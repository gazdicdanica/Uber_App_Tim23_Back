package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.Utils.GoogleMapUtils;
import com.uber.app.team23.AirRide.controller.WebSocketController;
import com.uber.app.team23.AirRide.dto.RideDTO;
import com.uber.app.team23.AirRide.dto.RideResponseDTO;
import com.uber.app.team23.AirRide.dto.UserShortDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.mapper.RideDTOMapper;
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
import java.util.Objects;


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
    @Autowired
    DriverService driverService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private VehicleRepository vehicleRepository;

    @Scheduled(fixedRate = 1000 * 60 * 2)
    public void scheduledRides() {
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
        Driver d = driverService.findById(driverId);
        Ride ride = rideRepository.findByDriverAndStatus(d, RideStatus.ACTIVE).orElse(null);
        if(ride != null){
            return new RideResponseDTO(ride);
        }return null;
    }

    public RideResponseDTO findActiveByPassenger(Long passengerId){
        Passenger p = passengerService.findOne(passengerId);
        Ride active = rideRepository.findByPassengersContainingAndStatus(p, RideStatus.ACTIVE).orElse(null);
        if(active != null){
            return new RideResponseDTO(active);
        }
        return null;
    }

    public Ride addPassengers(RideDTO rideDTO, Long rideId, Long userId){
        Ride ride = this.findOne(rideId);
        Passenger creator = passengerService.findOne(userId);
        System.err.println("Pukao Ovde");
        ride.addPassenger(creator);
        System.err.println("Broj ulinkovanih: "+rideDTO.getPassengers().size());
        for(UserShortDTO user: rideDTO.getPassengers()){
            System.err.println("Ili mozda ovde");
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
                List<Double> estimation = rideSchedulingService.getEstimates(route.getDeparture(), route.getDestination());
                int time =(int) Math.round(estimation.get(0)/60);
                estimatedTime += time;
                r = new Route();
                r.setDeparture(route.getDeparture());
                r.setDestination(route.getDestination());
                r.setDistance(estimation.get(1));
                distance += estimation.get(1);
                routeService.save(r);
            }else{
                distance += r.getDistance();
            }ride.getLocations().add(r);
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
        Passenger p = passengerService.findOne(passengerId);
        Ride ride = rideRepository.findByPassengersContainingAndStatus(p, RideStatus.PENDING).orElse(null);
        Ride accepted = rideRepository.findByPassengersContainingAndStatus(p, RideStatus.ACCEPTED).orElse(null);
        if(ride != null || accepted != null){
            throw new BadRequestException("Cannot create a ride while you have one already pending!");
        }
    }

    public void checkPassengerRideByEmail(String email) {
        Passenger p = passengerService.findByEmail(email);
        Ride ride = rideRepository.findByPassengersContainingAndStatus(p, RideStatus.PENDING).orElse(null);
        Ride accepted = rideRepository.findByPassengersContainingAndStatus(p, RideStatus.ACCEPTED).orElse(null);
        if (ride != null || accepted != null){
            throw new BadRequestException("Cannot create a ride while you have one already pending!");
        }
    }

    public Driver findPotentialDriver(Ride ride){
        return this.rideSchedulingService.findDriver(ride);
    }

    public Ride save(RideDTO rideDTO){
        for(UserShortDTO u : rideDTO.getPassengers()){
            this.checkPassengerRideByEmail(u.getEmail());
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
            ride = rideRepository.save(ride);
            return RideDTOMapper.fromRideToDTO(ride);
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
        ride = rideRepository.save(ride);
        return RideDTOMapper.fromRideToDTO(ride);
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

    public void updateLocations(RideStatus rideStatus) {
        if (rideStatus == RideStatus.ACCEPTED) {
            resolveLocationsUsingGoogle(findByStatus(rideStatus));

        } else {    //Active
            resolveLocationsUsingGoogle(findByStatus(RideStatus.ACTIVE));
        }
    }

    private void resolveLocationsUsingGoogle(List<Ride> rides) {
        for (Ride ride : rides) {
            Vehicle vehicle = ride.getVehicle();
            Location departure, destination;
            List<Route> routeList = new ArrayList<>(ride.getLocations());
            if (ride.getStatus() == RideStatus.ACCEPTED) {
                departure = vehicle.getCurrentLocation();
                destination = routeList.get(0).getDeparture();
                System.err.println("departure:" + departure + "\ndestination"+destination.getAddress());
            } else {    //Active
                departure = vehicle.getCurrentLocation();
                destination = routeList.get(routeList.size()-1).getDestination();
                System.err.println("departure:" + departure + "\ndestination"+destination.getAddress());
            }
            Location currentLoc = getLocAtTime(departure, destination);
            if (!Objects.equals(currentLoc.getLatitude(), destination.getLatitude()) ||
                    !Objects.equals(currentLoc.getLongitude(), destination.getLongitude())) {
                currentLoc.setAddress("");
                locationRepository.save(currentLoc);
                vehicle.setCurrentLocation(currentLoc);
                vehicleRepository.save(vehicle);

//                if(ride.getStatus() == RideStatus.ACCEPTED) {
//                    Location newDeparture = vehicle.getCurrentLocation();
//                    Location nextVehicleLocation = GoogleMapUtils.getLocationAtTime(newDeparture.getLatitude(), newDeparture.getLongitude(),
//                            destination.getLatitude(), destination.getLongitude());
//
//                    if (Objects.equals(nextVehicleLocation.getLatitude(), destination.getLatitude()) &&
//                            Objects.equals(nextVehicleLocation.getLongitude(), destination.getLongitude())) {
//
//                        //TODO notify vehicle arrived on address
//                    }
//                }
            }
        }
    }

    private Location getLocAtTime(Location departure, Location destination) {
        return GoogleMapUtils.getLocationAtTime(departure.getLatitude(), departure.getLongitude(),
                destination.getLatitude(), destination.getLongitude());
    }

}
