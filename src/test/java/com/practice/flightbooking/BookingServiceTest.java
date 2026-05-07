package com.practice.flightbooking;

import com.practice.flightbooking.dto.request.BookingRequestDTO;
import com.practice.flightbooking.dto.response.BookingResponseDTO;
import com.practice.flightbooking.entity.Booking;
import com.practice.flightbooking.entity.Flight;
import com.practice.flightbooking.entity.User;
import com.practice.flightbooking.enums.BookingStatus;
import com.practice.flightbooking.enums.FlightStatus;
import com.practice.flightbooking.enums.Role;
import com.practice.flightbooking.exception.SeatsNotAvailableException;
import com.practice.flightbooking.mapper.BookingMapper;
import com.practice.flightbooking.mapper.FlightMapper;
import com.practice.flightbooking.repo.BookingRepo;
import com.practice.flightbooking.repo.FlightRepo;
import com.practice.flightbooking.repo.UserRepo;
import com.practice.flightbooking.service.NotificationProducer;
import com.practice.flightbooking.service.BookingService;
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
public class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private FlightRepo flightRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private FlightMapper flightMapper;

    @Mock
    private NotificationProducer notificationProducer;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Flight flight;
    private BookingRequestDTO bookingRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@gmail.com");
        user.setRole(Role.CUSTOMER);

        flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("AI101");
        flight.setAirline("Air India");
        flight.setSource("Delhi");
        flight.setDestination("Mumbai");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setTotalSeats(150);
        flight.setAvailableSeats(150);
        flight.setPrice(4500.00);
        flight.setStatus(FlightStatus.SCHEDULED);

        bookingRequest = new BookingRequestDTO();
        bookingRequest.setFlightId(1L);
        bookingRequest.setSeatsBooked(2);
    }

    // ── Test 1: Book flight successfully ──────────────────────
    @Test
    void bookFlight_ShouldReturnBooking_WhenSeatsAvailable() {
        // ARRANGE
        when(userRepo.findByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));
        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setUser(user);
        savedBooking.setFlight(flight);
        savedBooking.setSeatsBooked(2);
        savedBooking.setTotalPrice(9000.0);
        savedBooking.setStatus(BookingStatus.CONFIRMED);
        when(bookingRepo.save(any(Booking.class))).thenReturn(savedBooking);

        BookingResponseDTO mockResponse = new BookingResponseDTO();
        mockResponse.setStatus("CONFIRMED");
        mockResponse.setTotalPrice(9000.0);
        mockResponse.setSeatsBooked(2);
        when(bookingMapper.entityToDTO(any(Booking.class))).thenReturn(mockResponse);

        // ACT
        BookingResponseDTO result = bookingService.bookFlight(bookingRequest, "john@gmail.com");

        // ASSERT
        assertNotNull(result);
        assertEquals(BookingStatus.CONFIRMED.name(), result.getStatus());
        assertEquals(9000.0, result.getTotalPrice());
        verify(bookingRepo, times(1)).save(any(Booking.class));
    }

    // ── Test 2: Book fails when not enough seats ───────────────
    @Test
    void bookFlight_ShouldThrowException_WhenNotEnoughSeats() {
        // ARRANGE
        flight.setAvailableSeats(1); // only 1 seat left
        bookingRequest.setSeatsBooked(5); // trying to book 5
        when(userRepo.findByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        when(flightRepo.findById(1L)).thenReturn(Optional.of(flight));

        // ACT & ASSERT
        assertThrows(SeatsNotAvailableException.class, () -> {
            bookingService.bookFlight(bookingRequest, "john@gmail.com");
        });
        verify(bookingRepo, never()).save(any(Booking.class));
    }

    // ── Test 3: Cancel booking successfully ───────────────────
    @Test
    void cancelBooking_ShouldCancelAndRestoreSeats() {
        // ARRANGE
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUser(user);
        booking.setFlight(flight);
        booking.setSeatsBooked(2);
        booking.setStatus(BookingStatus.CONFIRMED);
        flight.setAvailableSeats(148);

        when(userRepo.findByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        when(bookingRepo.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(Booking.class))).thenReturn(booking);

        BookingResponseDTO mockResponse = new BookingResponseDTO();
        mockResponse.setStatus("CANCELLED");
        when(bookingMapper.entityToDTO(any(Booking.class))).thenReturn(mockResponse);

        // ACT
        BookingResponseDTO result = bookingService.cancelBooking(1L, "john@gmail.com");

        // ASSERT
        assertEquals(BookingStatus.CANCELLED.name(), result.getStatus());
        assertEquals(150, flight.getAvailableSeats()); // seats restored
        verify(bookingRepo, times(1)).save(any(Booking.class));
    }
}