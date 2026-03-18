package com.parkingapp.service;

import com.parkingapp.dto.request.BookingRequest;
import com.parkingapp.model.*;
import com.parkingapp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ParkingLotService parkingLotService;

    @Transactional
    public Booking createBooking(BookingRequest request, User user) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new RuntimeException("End time must be after start time");
        }

        ParkingLot lot = parkingLotService.getById(request.getParkingLotId());

        if (lot.getAvailableSpots() <= 0) {
            throw new RuntimeException("No available spots in this parking lot");
        }

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                lot.getId(), request.getStartTime(), request.getEndTime());
        if (overlapping.size() >= lot.getTotalSpots()) {
            throw new RuntimeException("No spots available for the selected time slot");
        }

        long hours = Duration.between(request.getStartTime(), request.getEndTime()).toHours();
        if (hours == 0) hours = 1;
        BigDecimal totalPrice = lot.getPricePerHour().multiply(BigDecimal.valueOf(hours));

        Booking booking = Booking.builder()
                .user(user)
                .parkingLot(lot)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .build();

        parkingLotService.decrementAvailableSpots(lot);
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking getById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, User user) {
        Booking booking = getById(bookingId);

        if (!booking.getUser().getId().equals(user.getId()) && !user.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Not authorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        parkingLotService.incrementAvailableSpots(booking.getParkingLot());
        return bookingRepository.save(booking);
    }

    public void confirmBooking(Long bookingId) {
        Booking booking = getById(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
