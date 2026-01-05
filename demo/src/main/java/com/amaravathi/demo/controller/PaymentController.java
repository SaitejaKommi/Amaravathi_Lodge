package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Payment;
import com.amaravathi.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Create Razorpay order
    @PostMapping("/create-order/{bookingId}")
    public Map<String, Object> createPaymentOrder(@PathVariable Long bookingId) {
        return paymentService.createPaymentOrder(bookingId);
    }

    // Verify payment (called after successful payment)
    @PostMapping("/verify")
    public Map<String, Object> verifyPayment(@RequestBody Map<String, String> paymentData) {
        String razorpayOrderId = paymentData.get("razorpayOrderId");
        String razorpayPaymentId = paymentData.get("razorpayPaymentId");
        String razorpaySignature = paymentData.get("razorpaySignature");

        return paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
    }

    // Handle payment failure
    @PostMapping("/failure")
    public Map<String, Object> handlePaymentFailure(@RequestBody Map<String, String> paymentData) {
        String razorpayOrderId = paymentData.get("razorpayOrderId");
        String failureReason = paymentData.get("failureReason");

        return paymentService.handlePaymentFailure(razorpayOrderId, failureReason);
    }

    // Get payment by ID
    @GetMapping("/{id}")
    public Payment getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    // Get payment by booking
    @GetMapping("/booking/{bookingId}")
    public Payment getPaymentByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentByBooking(bookingId);
    }

    // Get all payments (admin/owner)
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    // Get successful payments
    @GetMapping("/status/success")
    public List<Payment> getSuccessfulPayments() {
        return paymentService.getSuccessfulPayments();
    }

    // Get failed payments
    @GetMapping("/status/failed")
    public List<Payment> getFailedPayments() {
        return paymentService.getFailedPayments();
    }
}