package com.uber.app.team23.AirRide.repository;

import com.uber.app.team23.AirRide.model.users.driverData.vehicleData.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

<<<<<<< Updated upstream
    @Query("select d from Document d where d.driver.id=?1")
    public Document findAllByDriverId(Long id);
=======
    public List<Document> findAllByDriver(Driver driver);
>>>>>>> Stashed changes
}
