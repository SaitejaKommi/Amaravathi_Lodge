package com.amaravathi.demo.repository;

import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUser(User user); // Get all bookings for a user
    List<Booking> findByUserId(Long userId); // Get all bookings by user ID
    List<Booking> findByStatus(String status); // Get bookings by status
    List<Booking> findByPaymentStatus(String paymentStatus); // Get bookings by payment status
}