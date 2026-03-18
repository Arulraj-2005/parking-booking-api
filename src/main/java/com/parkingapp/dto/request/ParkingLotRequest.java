package com.parkingapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ParkingLotRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String location;

    private String description;

    @NotNull @Min(1)
    private Integer totalSpots;

    @NotNull @DecimalMin("0.01")
    private BigDecimal pricePerHour;

    private Double latitude;
    private Double longitude;
}
