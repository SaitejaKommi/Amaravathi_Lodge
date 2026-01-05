package com.amaravathi.demo.repository;

import com.amaravathi.demo.model.Payment;
import com.amaravathi.demo.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByBooking(Booking booking); // Get payment for a booking
    Payment findByRazorpayOrderId(String razorpayOrderId); // Find by Razorpay order ID
    Payment findByRazorpayPaymentId(String razorpayPaymentId); // Find by Razorpay payment ID
    List<Payment> findByPaymentStatus(String paymentStatus); // Get payments by status
}