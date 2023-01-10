package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.repository.WorkingHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class WorkingHoursService {
    @Autowired
    WorkingHoursRepository workingHoursRepository;


    public WorkingHours findOne(Long id){
        WorkingHours workingHours = this.findById(id);
        if (workingHours == null){
            throw new EntityNotFoundException("Working hour does not exist");
        }
        return workingHours;
    }

    public WorkingHours findById(Long id){
        return workingHoursRepository.findById(id).orElse(null);
    }

    public WorkingHours save(Driver driver){
        LocalDateTime startShift = LocalDateTime.now();
        WorkingHours workingHours = new WorkingHours();
        workingHours.setStart(startShift);
        workingHours.setDriver(driver);
        return workingHoursRepository.save(workingHours);
    }

    public WorkingHours update(Long id){
        WorkingHours workingHours = this.findOne(id);
        if (workingHours.getEnd() != null){
            throw new BadRequestException("No shift is ongoing");
        }
        workingHours.setEnd(LocalDateTime.now());
        return workingHoursRepository.save(workingHours);
    }

    public List<WorkingHours> findByDriverInLastDay(Driver driver){
        return this.workingHoursRepository.findByDriverInLastDay(driver, LocalDateTime.now().minusDays(1));
    }

}
