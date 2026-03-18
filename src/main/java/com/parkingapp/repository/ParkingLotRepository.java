package com.parkingapp.repository;

import com.parkingapp.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {

    List<ParkingLot> findByActiveTrue();

    @Query("SELECT p FROM ParkingLot p WHERE p.active = true AND " +
           "LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%'))")
    List<ParkingLot> findByLocationContainingIgnoreCase(@Param("location") String location);

    @Query("SELECT p FROM ParkingLot p WHERE p.active = true AND p.availableSpots > 0")
    List<ParkingLot> findAvailable();

    @Query("SELECT p FROM ParkingLot p WHERE p.active = true AND " +
           "LOWER(p.location) LIKE LOWER(CONCAT('%', :location, '%')) AND p.availableSpots > 0")
    List<ParkingLot> findByLocationAndAvailable(@Param("location") String location);
}
