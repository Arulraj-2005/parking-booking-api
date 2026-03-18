package com.parkingapp.controller;

import com.parkingapp.dto.request.ParkingLotRequest;
import com.parkingapp.model.Booking;
import com.parkingapp.model.ParkingLot;
import com.parkingapp.model.User;
import com.parkingapp.repository.UserRepository;
import com.parkingapp.service.BookingService;
import com.parkingapp.service.ParkingLotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final ParkingLotService parkingLotService;
    private final BookingService bookingService;
    private final UserRepository userRepository;

    // Parking Lot Management
    @PostMapping("/parking-lots")
    public ResponseEntity<ParkingLot> createParkingLot(@Valid @RequestBody ParkingLotRequest request) {
        return ResponseEntity.ok(parkingLotService.create(request));
    }

    @PutMapping("/parking-lots/{id}")
    public ResponseEntity<ParkingLot> updateParkingLot(
            @PathVariable Long id,
            @Valid @RequestBody ParkingLotRequest request) {
        return ResponseEntity.ok(parkingLotService.update(id, request));
    }

    @DeleteMapping("/parking-lots/{id}")
    public ResponseEntity<Void> deactivateParkingLot(@PathVariable Long id) {
        parkingLotService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // Booking Management
    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @DeleteMapping("/bookings/{id}/cancel")
    public ResponseEntity<Booking> cancelBooking(
            @PathVariable Long id,
            @RequestAttribute("currentUser") User admin) {
        return ResponseEntity.ok(bookingService.cancelBooking(id, admin));
    }

    // User Management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }
}
