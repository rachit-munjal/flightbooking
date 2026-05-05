package com.practice.flightbooking.repo;

import com.practice.flightbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepo extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);
    Optional<Booking> findByIdAndUserId(Long id, Long userId);

}
