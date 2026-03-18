package com.parkingapp.repository;

import com.parkingapp.model.Booking;
import com.parkingapp.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByParkingLotId(Long parkingLotId);

    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.parkingLot.id = :lotId AND b.status != 'CANCELLED' AND " +
           "((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findOverlappingBookings(
        @Param("lotId") Long lotId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
}
