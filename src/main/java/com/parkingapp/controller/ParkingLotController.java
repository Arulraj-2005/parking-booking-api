package com.parkingapp.controller;

import com.parkingapp.model.ParkingLot;
import com.parkingapp.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @GetMapping
    public ResponseEntity<List<ParkingLot>> search(
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "false") boolean availableOnly) {
        return ResponseEntity.ok(parkingLotService.search(location, availableOnly));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getById(@PathVariable Long id) {
        return ResponseEntity.ok(parkingLotService.getById(id));
    }
}
