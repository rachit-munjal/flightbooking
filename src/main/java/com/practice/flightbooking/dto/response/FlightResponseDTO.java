package com.practice.flightbooking.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime departureTime;
//
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime arrivalTime;

    private String arrivalTime;
    private String departureTime;

    private Integer totalSeats;
    private Integer availableSeats;
    private Double price;
    private String status;
}
