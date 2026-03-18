package com.parkingapp.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingRequest {
    @NotNull
    private Long parkingLotId;

    @NotNull @Future
    private LocalDateTime startTime;

    @NotNull @Future
    private LocalDateTime endTime;
}
