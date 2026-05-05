package com.practice.flightbooking.service;

import com.practice.flightbooking.dto.request.FlightRequestDTO;
import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.entity.Flight;
import com.practice.flightbooking.enums.FlightStatus;
import com.practice.flightbooking.exception.FlightNotFoundException;
import com.practice.flightbooking.mapper.FlightMapper;
import com.practice.flightbooking.repo.FlightRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepo flightRepo;
    private final FlightMapper flightMapper;

    // Add new Flight
    public FlightResponseDTO addFlight(FlightRequestDTO request){
        if(flightRepo.existsByFlightNumber(request.getFlightNumber())){
            throw new RuntimeException("Flight Number already exists");
        }

        Flight flight = new Flight();
        flight.setFlightNumber(request.getFlightNumber());
        flight.setAirline(request.getAirline());
        flight.setSource(request.getSource());
        flight.setDestination(request.getDestination());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setTotalSeats(request.getTotalSeats());
        flight.setAvailableSeats(request.getTotalSeats());
        flight.setPrice(request.getPrice());
        flight.setStatus(FlightStatus.SCHEDULED);

        Flight saved = flightRepo.save(flight);
        return flightMapper.entityToDTO(saved);
    }

    // Update flight
    public FlightResponseDTO updateFlight(Long id, FlightRequestDTO request){
        Flight flight = flightRepo.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found"));

        flight.setAirline(request.getAirline());
        flight.setSource(request.getSource());
        flight.setDestination(request.getDestination());
        flight.setDepartureTime(request.getDepartureTime());
        flight.setArrivalTime(request.getArrivalTime());
        flight.setPrice(request.getPrice());

        return flightMapper.entityToDTO(flight);
    }

    public FlightResponseDTO findById(Long id){
        Flight flight = flightRepo.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight Not found"));
        return flightMapper.entityToDTO(flight);
    }

    // Delete Flight
    public void deleteFlight(Long id){
        Flight flight = flightRepo.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        flightRepo.delete(flight);
    }
}
