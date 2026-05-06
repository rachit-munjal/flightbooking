package com.practice.flightbooking.dto.response;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FlightResponseDTO implements Serializable {
    private Long id;
    private String flightNumber;
    private String airline;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double price;
    private String status;
}
