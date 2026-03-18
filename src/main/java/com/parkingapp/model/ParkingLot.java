package com.parkingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "parking_lots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    private String description;

    @Column(nullable = false)
    private Integer totalSpots;

    @Column(nullable = false)
    private Integer availableSpots;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    private Double latitude;
    private Double longitude;

    @Column(nullable = false)
    @Builder.Default
    private boolean active = true;

    @OneToMany(mappedBy = "parkingLot", cascade = CascadeType.ALL)
    private List<Booking> bookings;
}
