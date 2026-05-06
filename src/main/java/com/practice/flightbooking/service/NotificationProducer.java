package com.practice.flightbooking.service;

import com.practice.flightbooking.config.RabbitMQConfig;
import com.practice.flightbooking.dto.BookingNotificationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationProducer {

    private final AmqpTemplate amqpTemplate;

    public void sendBookingNotification(BookingNotificationDTO notification) {
        amqpTemplate.convertAndSend(
                RabbitMQConfig.BOOKING_EXCHANGE,
                RabbitMQConfig.BOOKING_ROUTING_KEY,
                notification
        );
        System.out.println("📨 Notification sent to RabbitMQ for booking: "
                + notification.getBookingId());
    }
}