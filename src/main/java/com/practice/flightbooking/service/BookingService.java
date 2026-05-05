package com.practice.flightbooking.service;

import com.practice.flightbooking.dto.request.BookingRequestDTO;
import com.practice.flightbooking.dto.response.BookingResponseDTO;
import com.practice.flightbooking.entity.Booking;
import com.practice.flightbooking.entity.Flight;
import com.practice.flightbooking.enums.BookingStatus;
import com.practice.flightbooking.exception.BookingNotFoundException;
import com.practice.flightbooking.exception.FlightNotFoundException;
import com.practice.flightbooking.exception.SeatsNotAvailableException;
import com.practice.flightbooking.exception.UserNotFoundException;
import com.practice.flightbooking.mapper.BookingMapper;
import com.practice.flightbooking.mapper.FlightMapper;
import com.practice.flightbooking.repo.BookingRepo;
import com.practice.flightbooking.repo.FlightRepo;
import com.practice.flightbooking.repo.UserRepo;
import com.practice.flightbooking.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final UserRepo userRepo;
    private final FlightRepo flightRepo;
    private final BookingRepo bookingRepo;
    private final BookingMapper bookingMapper;
    private final FlightMapper flightMapper;

    @Transactional
    public BookingResponseDTO bookFlight(BookingRequestDTO request, String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        Flight flight = flightRepo.findById(request.getFlightId()).orElseThrow(() -> new FlightNotFoundException("Flight not found"));
        if(flight.getAvailableSeats() < request.getSeatsBooked()){
            throw new SeatsNotAvailableException("Seats not available " + "Seats available are - " + flight.getAvailableSeats());
        }
        flight.setAvailableSeats(flight.getAvailableSeats() - request.getSeatsBooked());
        flightRepo.save(flight);

        Double totalPrice = flight.getPrice() * request.getSeatsBooked();

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setSeatsBooked(request.getSeatsBooked());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking saved = bookingRepo.save(booking);
        return bookingMapper.entityToDTO(saved);
    }

    public List<BookingResponseDTO> getMyBookings(String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        return bookingRepo.findByUserId(user.getId()).stream().map(bookingMapper::entityToDTO).collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long bookingId, String email){
        User user = userRepo.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        Booking booking = bookingRepo.findByIdAndUserId(bookingId, user.getId()).orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        if(booking.getStatus() == BookingStatus.CANCELLED){
            throw new BookingNotFoundException("Booking already cancelled");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        Flight flight = booking.getFlight();
        flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatsBooked());
        flightRepo.save(flight);

        return bookingMapper.entityToDTO(bookingRepo.save(booking));
    }

}
