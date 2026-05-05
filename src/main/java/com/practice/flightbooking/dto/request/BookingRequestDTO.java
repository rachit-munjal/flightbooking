package com.practice.flightbooking.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {

    @NotNull(message = "Flight Id is required")
    Long flightId;

    @NotNull(message = "Specify the number of seats booked")
    @Min(value = 1, message = "Atleast one seat is to be booked")
    private Integer seatsBooked;

}
