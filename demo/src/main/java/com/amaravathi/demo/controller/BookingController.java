package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Create room booking
    @PostMapping("/room/create")
    public Booking createRoomBooking(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        Long roomId = Long.parseLong(request.get("roomId").toString());
        LocalDateTime checkInDate = LocalDateTime.parse(request.get("checkInDate").toString());
        LocalDateTime checkOutDate = LocalDateTime.parse(request.get("checkOutDate").toString());

        return bookingService.createRoomBooking(userId, roomId, checkInDate, checkOutDate);
    }

    // Create dormitory booking
    @PostMapping("/dormitory/create")
    public Booking createDormitoryBooking(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        Long dormitoryId = Long.parseLong(request.get("dormitoryId").toString());
        LocalDateTime checkInDate = LocalDateTime.parse(request.get("checkInDate").toString());
        LocalDateTime checkOutDate = LocalDateTime.parse(request.get("checkOutDate").toString());

        return bookingService.createDormitoryBooking(userId, dormitoryId, checkInDate, checkOutDate);
    }

    // Get user's bookings
    @GetMapping("/user/{userId}")
    public List<Booking> getUserBookings(@PathVariable Long userId) {
        return bookingService.getUserBookings(userId);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    // Get all bookings (admin/owner)
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Apply discount/coupon to booking
    @PostMapping("/{bookingId}/apply-discount")
    public Booking applyDiscount(
            @PathVariable Long bookingId,
            @RequestParam Double discountAmount) {
        return bookingService.applyDiscount(bookingId, discountAmount);
    }

    // Mark booking as paid
    @PostMapping("/{bookingId}/pay")
    public Booking markAsPaid(@PathVariable Long bookingId) {
        return bookingService.markAsPaid(bookingId);
    }

    // Cancel booking
    @PostMapping("/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable Long bookingId) {
        return bookingService.cancelBooking(bookingId);
    }

    // Get today's bookings (for owner dashboard)
    @GetMapping("/today")
    public List<Booking> getTodaysBookings() {
        return bookingService.getTodaysBookings();
    }
}