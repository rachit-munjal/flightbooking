package com.practice.flightbooking.mapper;

import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.entity.Flight;
import org.springframework.stereotype.Component;

@Component
public class FlightMapper {

    public FlightResponseDTO entityToDTO(Flight flight){
        FlightResponseDTO response = new FlightResponseDTO();
        response.setId(flight.getId());
        response.setFlightNumber(flight.getFlightNumber());
        response.setAirline(flight.getAirline());
        response.setSource(flight.getSource());
        response.setDestination(flight.getDestination());
        response.setDepartureTime(flight.getDepartureTime());
        response.setArrivalTime(flight.getArrivalTime());
        response.setTotalSeats(flight.getTotalSeats());
        response.setAvailableSeats(flight.getAvailableSeats());
        response.setPrice(flight.getPrice());
        response.setStatus(flight.getStatus().name());
        return response;
    }
}
