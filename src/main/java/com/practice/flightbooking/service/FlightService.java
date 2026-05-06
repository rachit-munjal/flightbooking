package com.practice.flightbooking.service;

import com.practice.flightbooking.dto.request.FlightRequestDTO;
import com.practice.flightbooking.dto.response.FlightResponseDTO;
import com.practice.flightbooking.entity.Flight;
import com.practice.flightbooking.enums.FlightStatus;
import com.practice.flightbooking.exception.FlightNotFoundException;
import com.practice.flightbooking.mapper.FlightMapper;
import com.practice.flightbooking.repo.FlightRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepo flightRepo;
    private final FlightMapper flightMapper;

    // Add new Flight
    @CacheEvict(value = "flights", allEntries = true)
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
    @CacheEvict(value = "flights", allEntries = true)
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
    @CacheEvict(value = "flights", allEntries = true)
    public void deleteFlight(Long id){
        Flight flight = flightRepo.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        flightRepo.delete(flight);
    }

    @Cacheable(value = "flights", key = "#source + '-' + #destination")
    public List<FlightResponseDTO> searchFlights(String source, String destination,
                                                 int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureTime").ascending());
        return flightRepo
                .findBySourceIgnoreCaseAndDestinationIgnoreCase(source, destination, pageable)
                .getContent() // convert Page to List
                .stream()
                .map(flightMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    // ── Parallel Search using CompletableFuture ───────────────
//    public List<FlightResponseDTO> parallelSearch(String source, String destination) {
//
//        // search 1 — all flights on this route
//        CompletableFuture<List<Flight>> allFlights = CompletableFuture.supplyAsync(() ->
//                flightRepo.findBySourceIgnoreCaseAndDestinationIgnoreCase(
//                        source, destination)
//        );
//
//        // search 2 — IndiGo flights on this route
//        CompletableFuture<List<Flight>> indigoFlights = CompletableFuture.supplyAsync(() ->
//                flightRepo.findBySourceIgnoreCaseAndDestinationIgnoreCaseAndAirline(
//                        source, destination, "IndiGo")
//        );
//
//        // search 3 — Air India flights on this route
//        CompletableFuture<List<Flight>> airIndiaFlights = CompletableFuture.supplyAsync(() ->
//                flightRepo.findBySourceIgnoreCaseAndDestinationIgnoreCaseAndAirline(
//                        source, destination, "Air India")
//        );
//
//        // wait for all 3 to finish simultaneously
//        CompletableFuture.allOf(allFlights, indigoFlights, airIndiaFlights).join();
//
//        // combine and remove duplicates
//        return allFlights.join()
//                .stream()
//                .map(flightMapper::entityToDTO)
//                .distinct()
//                .collect(Collectors.toList());
//    }
}
