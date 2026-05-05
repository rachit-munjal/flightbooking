package com.practice.flightbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightRequestDTO {

    @NotBlank(message = "Airline is required")
    private String airline;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Departure Time is needed")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival Time is needed")
    private LocalDateTime arrivalTime;

    @NotNull(message = "Total Seats is needed")
    @Min(value = 1, message = "At least 1 seat required")
    private Integer totalSeats;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;
}
