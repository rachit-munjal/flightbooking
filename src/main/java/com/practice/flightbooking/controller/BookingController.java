package com.practice.flightbooking.controller;

import com.practice.flightbooking.dto.request.BookingRequestDTO;
import com.practice.flightbooking.dto.response.BookingResponseDTO;
import com.practice.flightbooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/book")
    public ResponseEntity<BookingResponseDTO> bookFlight(@Valid @RequestBody BookingRequestDTO request, Principal principal){
        return ResponseEntity.ok(bookingService.bookFlight(request, principal.getName()));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDTO>> getMyBookings(
            Principal principal) {
        return ResponseEntity.ok(
                bookingService.getMyBookings(principal.getName())
        );
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id, Principal principal){
        return ResponseEntity.ok(bookingService.cancelBooking(id, principal.getName()));
    }
}
