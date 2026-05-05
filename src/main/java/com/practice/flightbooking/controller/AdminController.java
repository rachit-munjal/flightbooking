package com.practice.flightbooking.controller;

import com.practice.flightbooking.dto.request.FlightRequestDTO;
import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final FlightService flightService;

    @PostMapping("/flights")
    public ResponseEntity<FlightResponseDTO> addFlight(@Valid @RequestBody FlightRequestDTO flight){
        FlightResponseDTO flightResponseDTO = flightService.addFlight(flight);
        return ResponseEntity.ok(flightResponseDTO);
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(@PathVariable Long id, @Valid @RequestBody FlightRequestDTO flightRequestDTO){
        return ResponseEntity.ok(flightService.updateFlight(id, flightRequestDTO));
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id){
        flightService.deleteFlight(id);
        return ResponseEntity.ok("Flight deleted Successfully");
    }
}
