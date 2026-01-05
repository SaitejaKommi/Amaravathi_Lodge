package com.amaravathi.demo.service;

import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.model.Review;
import com.amaravathi.demo.repository.BookingRepository;
import com.amaravathi.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class OwnerService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    // Get today's bookings
    public List<Booking> getTodaysBookings() {
        List<Booking> allBookings = bookingRepository.findAll();
        LocalDateTime today = LocalDateTime.now();

        return allBookings.stream()
                .filter(b -> b.getCheckInDate().toLocalDate().equals(today.toLocalDate()))
                .toList();
    }

    // Get bookings for a specific month
    public List<Booking> getMonthlyBookings(int year, int month) {
        List<Booking> allBookings = bookingRepository.findAll();
        YearMonth yearMonth = YearMonth.of(year, month);

        return allBookings.stream()
                .filter(b -> YearMonth.from(b.getCheckInDate()).equals(yearMonth))
                .toList();
    }

    // Calculate today's earnings
    public Double getTodaysEarnings() {
        List<Booking> todaysBookings = getTodaysBookings();

        return todaysBookings.stream()
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    // Calculate monthly earnings
    public Double getMonthlyEarnings(int year, int month) {
        List<Booking> monthlyBookings = getMonthlyBookings(year, month);

        return monthlyBookings.stream()
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    // Calculate yearly earnings
    public Double getYearlyEarnings(int year) {
        List<Booking> allBookings = bookingRepository.findAll();

        return allBookings.stream()
                .filter(b -> b.getCheckInDate().getYear() == year)
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    // Get total earnings (all time)
    public Double getTotalEarnings() {
        List<Booking> allBookings = bookingRepository.findAll();

        return allBookings.stream()
                .filter(b -> "PAID".equals(b.getPaymentStatus()))
                .mapToDouble(Booking::getTotalPrice)
                .sum();
    }

    // Get dashboard overview
    public Map<String, Object> getDashboardOverview() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        List<Booking> allBookings = bookingRepository.findAll();
        List<Booking> todaysBookings = getTodaysBookings();
        List<Booking> monthlyBookings = getMonthlyBookings(year, month);
        List<Review> allReviews = reviewRepository.findAll();

        // Count bookings
        long totalBookings = allBookings.size();
        long todayBookings = todaysBookings.size();
        long monthBookings = monthlyBookings.size();
        long completedBookings = allBookings.stream()
                .filter(b -> "COMPLETED".equals(b.getStatus()))
                .count();
        long pendingPayments = allBookings.stream()
                .filter(b -> "PENDING".equals(b.getPaymentStatus()))
                .count();

        // Count reviews
        long totalReviews = allReviews.size();
        long pendingReplies = allReviews.stream()
                .filter(b -> b.getOwnerReply() == null)
                .count();

        // Calculate average rating
        Double avgRating = allReviews.isEmpty() ? 0.0 :
                allReviews.stream()
                        .mapToDouble(Review::getRating)
                        .average()
                        .orElse(0.0);

        // Earnings
        Double todayEarnings = getTodaysEarnings();
        Double monthEarnings = getMonthlyEarnings(year, month);
        Double yearEarnings = getYearlyEarnings(year);
        Double totalEarnings = getTotalEarnings();

        Map<String, Object> dashboard = new HashMap<>();

        // Bookings section
        dashboard.put("totalBookings", totalBookings);
        dashboard.put("todayBookings", todayBookings);
        dashboard.put("monthBookings", monthBookings);
        dashboard.put("completedBookings", completedBookings);
        dashboard.put("pendingPayments", pendingPayments);

        // Reviews section
        dashboard.put("totalReviews", totalReviews);
        dashboard.put("pendingReplies", pendingReplies);
        dashboard.put("averageRating", Math.round(avgRating * 100.0) / 100.0); // Round to 2 decimals

        // Earnings section
        dashboard.put("todayEarnings", todayEarnings);
        dashboard.put("monthEarnings", monthEarnings);
        dashboard.put("yearEarnings", yearEarnings);
        dashboard.put("totalEarnings", totalEarnings);

        // Meta info
        dashboard.put("currentMonth", month);
        dashboard.put("currentYear", year);
        dashboard.put("lastUpdated", LocalDateTime.now());

        return dashboard;
    }

    // Get earnings breakdown by month (last 12 months)
    public Map<String, Double> getEarningsBreakdown() {
        Map<String, Double> breakdown = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        for (int i = 11; i >= 0; i--) {
            LocalDateTime date = now.minusMonths(i);
            int year = date.getYear();
            int month = date.getMonthValue();

            String monthKey = year + "-" + String.format("%02d", month);
            Double earnings = getMonthlyEarnings(year, month);
            breakdown.put(monthKey, earnings);
        }

        return breakdown;
    }

    // Get top performing rooms (most bookings)
    public Map<String, Long> getTopRooms() {
        Map<String, Long> roomBookings = new HashMap<>();
        List<Booking> allBookings = bookingRepository.findAll();

        allBookings.stream()
                .filter(b -> b.getRoom() != null)
                .forEach(b -> {
                    String roomNumber = b.getRoom().getRoomNumber();
                    roomBookings.put(roomNumber, roomBookings.getOrDefault(roomNumber, 0L) + 1);
                });

        return roomBookings;
    }

    // Get booking status breakdown
    public Map<String, Long> getBookingStatusBreakdown() {
        Map<String, Long> statusBreakdown = new HashMap<>();
        List<Booking> allBookings = bookingRepository.findAll();

        statusBreakdown.put("CONFIRMED",
                allBookings.stream().filter(b -> "CONFIRMED".equals(b.getStatus())).count());
        statusBreakdown.put("COMPLETED",
                allBookings.stream().filter(b -> "COMPLETED".equals(b.getStatus())).count());
        statusBreakdown.put("CANCELLED",
                allBookings.stream().filter(b -> "CANCELLED".equals(b.getStatus())).count());

        return statusBreakdown;
    }

    // Get payment status breakdown
    public Map<String, Long> getPaymentStatusBreakdown() {
        Map<String, Long> paymentBreakdown = new HashMap<>();
        List<Booking> allBookings = bookingRepository.findAll();

        paymentBreakdown.put("PAID",
                allBookings.stream().filter(b -> "PAID".equals(b.getPaymentStatus())).count());
        paymentBreakdown.put("PENDING",
                allBookings.stream().filter(b -> "PENDING".equals(b.getPaymentStatus())).count());

        return paymentBreakdown;
    }

    // Get reviews pending replies
    public List<Review> getPendingReviews() {
        List<Review> allReviews = reviewRepository.findAll();

        return allReviews.stream()
                .filter(r -> r.getOwnerReply() == null)
                .toList();
    }
}