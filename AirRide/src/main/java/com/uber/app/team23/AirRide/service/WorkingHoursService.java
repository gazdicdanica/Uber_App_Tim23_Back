package com.uber.app.team23.AirRide.service;

import com.uber.app.team23.AirRide.dto.WorkHoursDTO;
import com.uber.app.team23.AirRide.exceptions.BadRequestException;
import com.uber.app.team23.AirRide.exceptions.EntityNotFoundException;
import com.uber.app.team23.AirRide.model.users.driverData.Driver;
import com.uber.app.team23.AirRide.model.users.driverData.WorkingHours;
import com.uber.app.team23.AirRide.repository.WorkingHoursRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
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

    public WorkingHours save(Driver driver, WorkHoursDTO workHoursDTO){

        if(this.calculateWorkingHours(driver) > 8){
            throw new BadRequestException("Cannot start shift because you exceeded the 8 hours limit in last 24 hours!");
        }

        WorkingHours ongoing = workingHoursRepository.findShiftInProgress(driver).orElse(null);
        if(ongoing != null){
            throw  new BadRequestException("Shifth already ongoing!");
        }

        LocalDateTime startShift;
        if(workHoursDTO == null){
            startShift = LocalDateTime.now();
        }else{
            startShift = workHoursDTO.getStart();
        }
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

    public Page<WorkingHours> findAll(Pageable pageable) {
        return workingHoursRepository.findAll(pageable);
    }

    public List<WorkingHours> findByDriverInLastDay(Driver driver){
        return this.workingHoursRepository.findByDriverInLastDay(driver, LocalDateTime.now().minusDays(1));
    }

    public int calculateWorkingHours(Driver driver){
        int hours = 0;
        List<WorkingHours> workingHours = findByDriverInLastDay(driver);
        for(WorkingHours wh : workingHours){
            if(wh.getEnd() != null){
                hours += Math.abs(Duration.between(wh.getEnd(), wh.getStart()).toHours());
            }else{
                hours += Math.abs(Duration.between(wh.getStart(), LocalDateTime.now()).toHours());
            }
        }
        return hours;
    }

    public WorkingHours endWorkingHours(Driver driver){
        WorkingHours workingHours = workingHoursRepository.findShiftInProgress(driver).orElseThrow(() -> new BadRequestException("No shift is ongoing"));
        workingHours.setEnd(LocalDateTime.now());
        return workingHoursRepository.save(workingHours);
    }

}
