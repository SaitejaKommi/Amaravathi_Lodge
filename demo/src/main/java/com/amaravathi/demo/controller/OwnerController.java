package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.model.Review;
import com.amaravathi.demo.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    // Get complete dashboard overview
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return ownerService.getDashboardOverview();
    }

    // Get today's bookings
    @GetMapping("/bookings/today")
    public List<Booking> getTodaysBookings() {
        return ownerService.getTodaysBookings();
    }

    // Get monthly bookings
    @GetMapping("/bookings/month/{year}/{month}")
    public List<Booking> getMonthlyBookings(
            @PathVariable int year,
            @PathVariable int month) {
        return ownerService.getMonthlyBookings(year, month);
    }

    // Get today's earnings
    @GetMapping("/earnings/today")
    public Map<String, Object> getTodaysEarnings() {
        Double earnings = ownerService.getTodaysEarnings();
        return Map.of(
                "date", "today",
                "earnings", earnings
        );
    }

    // Get monthly earnings
    @GetMapping("/earnings/month/{year}/{month}")
    public Map<String, Object> getMonthlyEarnings(
            @PathVariable int year,
            @PathVariable int month) {
        Double earnings = ownerService.getMonthlyEarnings(year, month);
        return Map.of(
                "year", year,
                "month", month,
                "earnings", earnings
        );
    }

    // Get yearly earnings
    @GetMapping("/earnings/year/{year}")
    public Map<String, Object> getYearlyEarnings(@PathVariable int year) {
        Double earnings = ownerService.getYearlyEarnings(year);
        return Map.of(
                "year", year,
                "earnings", earnings
        );
    }

    // Get total earnings (all time)
    @GetMapping("/earnings/total")
    public Map<String, Object> getTotalEarnings() {
        Double earnings = ownerService.getTotalEarnings();
        return Map.of(
                "earnings", earnings
        );
    }

    // Get earnings breakdown (last 12 months)
    @GetMapping("/earnings/breakdown")
    public Map<String, Double> getEarningsBreakdown() {
        return ownerService.getEarningsBreakdown();
    }

    // Get top performing rooms
    @GetMapping("/rooms/top")
    public Map<String, Long> getTopRooms() {
        return ownerService.getTopRooms();
    }

    // Get booking status breakdown
    @GetMapping("/bookings/status-breakdown")
    public Map<String, Long> getBookingStatusBreakdown() {
        return ownerService.getBookingStatusBreakdown();
    }

    // Get payment status breakdown
    @GetMapping("/bookings/payment-breakdown")
    public Map<String, Long> getPaymentStatusBreakdown() {
        return ownerService.getPaymentStatusBreakdown();
    }

    // Get reviews pending replies
    @GetMapping("/reviews/pending")
    public List<Review> getPendingReviews() {
        return ownerService.getPendingReviews();
    }
}