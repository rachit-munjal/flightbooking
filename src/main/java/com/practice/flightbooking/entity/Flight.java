package com.practice.flightbooking.entity;

import com.practice.flightbooking.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String flightNumber;

    private String airline;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private String destination;

    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    private Integer totalSeats;
    private Integer availableSeats;

    private Double price;

    @Enumerated(EnumType.STRING)
    private FlightStatus status;

    @Version
    private Integer version;
}
