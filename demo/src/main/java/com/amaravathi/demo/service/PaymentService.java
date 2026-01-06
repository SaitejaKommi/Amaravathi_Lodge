package com.amaravathi.demo.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import com.amaravathi.demo.model.Payment;
import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.model.User;
import com.amaravathi.demo.repository.PaymentRepository;
import com.amaravathi.demo.repository.BookingRepository;
import com.amaravathi.demo.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    // Razorpay credentials (CHANGE THESE WITH YOUR ACTUAL KEYS)
    private static final String RAZORPAY_KEY_ID = "rzp_test_S0Rp77BVEXcnhF";
    private static final String RAZORPAY_KEY_SECRET = "mrarccIPV0ifi7nDLgKoS0G2";

    // Create Razorpay order
    public Map<String, Object> createPaymentOrder(Long bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId).orElse(null);
            if (booking == null) {
                throw new RuntimeException("Booking not found!");
            }

            User user = booking.getUser();

            // Initialize Razorpay client
            RazorpayClient razorpay = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

            // Create order
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (long)(booking.getTotalPrice() * 100)); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "booking_" + bookingId);
            orderRequest.put("notes", new JSONObject()
                    .put("bookingId", bookingId)
                    .put("userId", user.getId())
                    .put("userEmail", user.getEmail())
            );

            Order order = razorpay.orders.create(orderRequest);
            String orderId = order.get("id");

            // Save payment record
            Payment payment = new Payment();
            payment.setBooking(booking);
            payment.setUser(user);
            payment.setAmount(booking.getTotalPrice());
            payment.setRazorpayOrderId(orderId);
            payment.setPaymentStatus("PENDING");
            payment.setPaymentMethod("RAZORPAY");
            payment.setCreatedAt(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // Return order details
            Map<String, Object> response = new HashMap<>();
            response.put("orderId", orderId);
            response.put("amount", booking.getTotalPrice());
            response.put("keyId", RAZORPAY_KEY_ID);
            response.put("bookingId", bookingId);
            response.put("userName", user.getName());
            response.put("userEmail", user.getEmail());
            response.put("userPhone", user.getPhone());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage());
        }
    }

    // Verify payment and update booking
    public Map<String, Object> verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        try {
            // Verify signature
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", razorpayOrderId);
            options.put("razorpay_payment_id", razorpayPaymentId);
            options.put("razorpay_signature", razorpaySignature);

            boolean isValidSignature = Utils.verifyPaymentSignature(options, RAZORPAY_KEY_SECRET);

            if (!isValidSignature) {
                throw new RuntimeException("Invalid payment signature!");
            }

            // Get payment record
            Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
            if (payment == null) {
                throw new RuntimeException("Payment record not found!");
            }

            // Update payment
            payment.setRazorpayPaymentId(razorpayPaymentId);
            payment.setRazorpaySignature(razorpaySignature);
            payment.setPaymentStatus("SUCCESS");
            payment.setPaymentTime(LocalDateTime.now());
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // Update booking payment status
            Booking booking = payment.getBooking();
            booking.setPaymentStatus("PAID");
            booking.setUpdatedAt(LocalDateTime.now());
            bookingRepository.save(booking);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment verified successfully!");
            response.put("paymentId", razorpayPaymentId);
            response.put("bookingId", booking.getId());

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Payment verification failed: " + e.getMessage());
        }
    }

    // Handle payment failure
    public Map<String, Object> handlePaymentFailure(String razorpayOrderId, String failureReason) {
        try {
            Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId);
            if (payment == null) {
                throw new RuntimeException("Payment record not found!");
            }

            // Update payment status
            payment.setPaymentStatus("FAILED");
            payment.setFailureReason(failureReason);
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // Booking status remains CONFIRMED, payment status is PENDING

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Payment failure recorded");
            response.put("failureReason", failureReason);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error handling payment failure: " + e.getMessage());
        }
    }

    // Get payment by ID
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }

    // Get payment by booking
    public Payment getPaymentByBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }
        return paymentRepository.findByBooking(booking);
    }

    // Get all payments
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // Get successful payments
    public List<Payment> getSuccessfulPayments() {
        return paymentRepository.findByPaymentStatus("SUCCESS");
    }

    // Get failed payments
    public List<Payment> getFailedPayments() {
        return paymentRepository.findByPaymentStatus("FAILED");
    }
}