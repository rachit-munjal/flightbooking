package com.practice.flightbooking;

import com.practice.flightbooking.dto.request.FlightRequestDTO;
import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.entity.Flight;
import com.practice.flightbooking.enums.FlightStatus;
import com.practice.flightbooking.exception.FlightNotFoundException;
import com.practice.flightbooking.mapper.FlightMapper;
import com.practice.flightbooking.repo.FlightRepo;
import com.practice.flightbooking.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightServiceTest {

    @Mock
    private FlightRepo flightRepo;

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightService flightService;

    private FlightRequestDTO flightRequest;
    private Flight flight;

    @BeforeEach
    void setUp() {
        flightRequest = new FlightRequestDTO();
        flightRequest.setFlightNumber("AI101");
        flightRequest.setAirline("Air India");
        flightRequest.setSource("Delhi");
        flightRequest.setDestination("Mumbai");
        flightRequest.setDepartureTime(LocalDateTime.now().plusDays(1));
        flightRequest.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        flightRequest.setTotalSeats(150);
        flightRequest.setPrice(4500.00);

        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("AI101");
        flight.setAirline("Air India");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setTotalSeats(150);
        flight.setAvailableSeats(150);
        flight.setPrice(4500.00);
        flight.setStatus(FlightStatus.SCHEDULED);
    }

    // ── Test 1: Add flight successfully ───────────────────────
    @Test
    void addFlight_ShouldReturnFlight_WhenFlightNumberNotExists() {
        // ARRANGE
        when(flightRepo.existsByFlightNumber("AI101")).thenReturn(false);
        when(flightRepo.save(any(Flight.class))).thenReturn(flight);

        FlightResponseDTO flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setFlightNumber("AI101");
        flightResponseDTO.setAirline("Air India");
        flightResponseDTO.setSource("Delhi");
        flightResponseDTO.setDestination("Mumbai");
        flightResponseDTO.setPrice(4500.00);
        flightResponseDTO.setStatus("SCHEDULED");

        when(flightMapper.entityToDTO(any(Flight.class))).thenReturn(flightResponseDTO);

        // ACT
        FlightResponseDTO result = flightService.addFlight(flightRequest);

        // ASSERT
        assertNotNull(result);
        assertEquals("AI101", result.getFlightNumber());
        assertEquals("Air India", result.getAirline());
        verify(flightRepo, times(1)).save(any(Flight.class));
    }

    // ── Test 2: Add flight fails if number exists ──────────────
    @Test
    void addFlight_ShouldThrowException_WhenFlightNumberExists() {
        // ARRANGE
        when(flightRepo.existsByFlightNumber("AI101")).thenReturn(true);

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> {
            flightService.addFlight(flightRequest);
        });
        verify(flightRepo, never()).save(any(Flight.class));
    }

    // ── Test 3: Get flight by id ───────────────────────────────
    @Test
    void getFlightById_ShouldReturnFlight_WhenExists() {
        // ARRANGE
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

        FlightResponseDTO flightResponseDTO = new FlightResponseDTO();
        flightResponseDTO.setFlightNumber("AI101");
        flightResponseDTO.setAirline("Air India");
        flightResponseDTO.setSource("Delhi");
        flightResponseDTO.setDestination("Mumbai");
        flightResponseDTO.setPrice(4500.00);
        flightResponseDTO.setStatus("SCHEDULED");

        when(flightMapper.entityToDTO(any(Flight.class))).thenReturn(flightResponseDTO);

        // ACT
        FlightResponseDTO result = flightService.findById(1L);

        // ASSERT
        assertNotNull(result);
        assertEquals("AI101", result.getFlightNumber());
    }

    // ── Test 4: Get flight throws exception if not found ───────
    @Test
    void getFlightById_ShouldThrowException_WhenNotFound() {
        // ARRANGE
        when(flightRepo.findById(99L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(FlightNotFoundException.class, () -> {
            flightService.findById(99L);
        });
    }
}