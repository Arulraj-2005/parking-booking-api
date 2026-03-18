package com.parkingapp.service;

import com.parkingapp.dto.request.ParkingLotRequest;
import com.parkingapp.model.ParkingLot;
import com.parkingapp.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    public List<ParkingLot> getAllActive() {
        return parkingLotRepository.findByActiveTrue();
    }

    public List<ParkingLot> search(String location, boolean availableOnly) {
        if (location != null && !location.isBlank()) {
            return availableOnly
                    ? parkingLotRepository.findByLocationAndAvailable(location)
                    : parkingLotRepository.findByLocationContainingIgnoreCase(location);
        }
        return availableOnly
                ? parkingLotRepository.findAvailable()
                : parkingLotRepository.findByActiveTrue();
    }

    public ParkingLot getById(Long id) {
        return parkingLotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parking lot not found with id: " + id));
    }

    public ParkingLot create(ParkingLotRequest request) {
        ParkingLot lot = ParkingLot.builder()
                .name(request.getName())
                .location(request.getLocation())
                .description(request.getDescription())
                .totalSpots(request.getTotalSpots())
                .availableSpots(request.getTotalSpots())
                .pricePerHour(request.getPricePerHour())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();
        return parkingLotRepository.save(lot);
    }

    public ParkingLot update(Long id, ParkingLotRequest request) {
        ParkingLot lot = getById(id);
        lot.setName(request.getName());
        lot.setLocation(request.getLocation());
        lot.setDescription(request.getDescription());
        lot.setPricePerHour(request.getPricePerHour());
        lot.setLatitude(request.getLatitude());
        lot.setLongitude(request.getLongitude());
        return parkingLotRepository.save(lot);
    }

    public void deactivate(Long id) {
        ParkingLot lot = getById(id);
        lot.setActive(false);
        parkingLotRepository.save(lot);
    }

    public void decrementAvailableSpots(ParkingLot lot) {
        if (lot.getAvailableSpots() <= 0) {
            throw new RuntimeException("No available spots in this parking lot");
        }
        lot.setAvailableSpots(lot.getAvailableSpots() - 1);
        parkingLotRepository.save(lot);
    }

    public void incrementAvailableSpots(ParkingLot lot) {
        if (lot.getAvailableSpots() < lot.getTotalSpots()) {
            lot.setAvailableSpots(lot.getAvailableSpots() + 1);
            parkingLotRepository.save(lot);
        }
    }
}
