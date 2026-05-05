package com.practice.flightbooking.mapper;

import com.practice.flightbooking.dto.response.BookingResponseDTO;
import com.practice.flightbooking.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {
    public BookingResponseDTO entityToDTO(Booking booking) {
        BookingResponseDTO response = new BookingResponseDTO();
        response.setId(booking.getId());
        response.setFlightNumber(booking.getFlight().getFlightNumber());
        response.setAirline(booking.getFlight().getAirline());
        response.setSource(booking.getFlight().getSource());
        response.setDestination(booking.getFlight().getDestination());
        response.setDepartureTime(booking.getFlight().getDepartureTime());
        response.setSeatsBooked(booking.getSeatsBooked());
        response.setTotalPrice(booking.getTotalPrice());
        response.setStatus(booking.getStatus().name());
        response.setBookedAt(booking.getBookedAt());
        return response;
    }
}
