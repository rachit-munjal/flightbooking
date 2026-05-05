package com.practice.flightbooking.controller;

import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(flightService.findById(id));
    }
}