package com.practice.flightbooking.controller;

import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    @GetMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(flightService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FlightResponseDTO>> searchFlights(
            @RequestParam String source,
            @RequestParam String destination,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                flightService.searchFlights(source, destination, page, size)
        );
    }

//    @GetMapping("/parallel-search")
//    public ResponseEntity<List<FlightResponseDTO>> parallelSearch(
//            @RequestParam String source,
//            @RequestParam String destination) {
//        return ResponseEntity.ok(
//                flightService.parallelSearch(source, destination)
//        );
//    }
}