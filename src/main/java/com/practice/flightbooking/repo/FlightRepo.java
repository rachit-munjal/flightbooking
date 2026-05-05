package com.practice.flightbooking.repo;


import com.practice.flightbooking.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FlightRepo extends JpaRepository<Flight, Long> {

    Page<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source, String destination, Pageable pageable);
    List<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCase(String source, String destination);
    List<Flight> findBySourceIgnoreCaseAndDestinationIgnoreCaseAndAirline(String source, String destination, String airline);
    boolean existsByFlightNumber(String flightNumber);
}
