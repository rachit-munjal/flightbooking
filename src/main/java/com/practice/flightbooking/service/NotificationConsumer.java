package com.practice.flightbooking.service;

import com.practice.flightbooking.config.RabbitMQConfig;
import com.practice.flightbooking.dto.BookingNotificationDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @RabbitListener(queues = RabbitMQConfig.BOOKING_QUEUE)
    public void receiveBookingNotification(
            @Payload BookingNotificationDTO notification) { // ADD @Payload
        System.out.println("====================================");
        System.out.println("✅ BOOKING CONFIRMATION NOTIFICATION");
        System.out.println("====================================");
        System.out.println("Booking ID   : " + notification.getBookingId());
        System.out.println("Customer     : " + notification.getCustomerName());
        System.out.println("Email        : " + notification.getCustomerEmail());
        System.out.println("Flight       : " + notification.getFlightNumber());
        System.out.println("Airline      : " + notification.getAirline());
        System.out.println("Route        : " + notification.getSource()
                + " → " + notification.getDestination());
        System.out.println("Departure    : " + notification.getDepartureTime());
        System.out.println("Seats Booked : " + notification.getSeatsBooked());
        System.out.println("Total Price  : ₹" + notification.getTotalPrice());
        System.out.println("====================================");
    }
}