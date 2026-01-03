package com.amaravathi.demo.service;

import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.model.User;
import com.amaravathi.demo.model.Room;
import com.amaravathi.demo.model.Dormitory;
import com.amaravathi.demo.repository.BookingRepository;
import com.amaravathi.demo.repository.UserRepository;
import com.amaravathi.demo.repository.RoomRepository;
import com.amaravathi.demo.repository.DormitoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DormitoryRepository dormitoryRepository;

    // Create booking for a room
    public Booking createRoomBooking(Long userId, Long roomId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {

        // Validate user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        // Validate room exists
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            throw new RuntimeException("Room not found!");
        }

        // Validate room is available
        if (!room.getIsAvailable()) {
            throw new RuntimeException("Room is not available!");
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) {
            throw new RuntimeException("Check-out date must be after check-in date!");
        }

        // Calculate total price
        Double totalPrice = room.getPrice() * nights;

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");
        booking.setPaymentStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        // Mark room as unavailable
        room.setIsAvailable(false);
        roomRepository.save(room);

        // Save booking
        return bookingRepository.save(booking);
    }

    // Create booking for a dormitory bed
    public Booking createDormitoryBooking(Long userId, Long dormitoryId, LocalDateTime checkInDate, LocalDateTime checkOutDate) {

        // Validate user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        // Validate dormitory bed exists
        Dormitory dormitory = dormitoryRepository.findById(dormitoryId).orElse(null);
        if (dormitory == null) {
            throw new RuntimeException("Dormitory bed not found!");
        }

        // Validate bed is available
        if (!dormitory.getIsAvailable()) {
            throw new RuntimeException("Bed is not available!");
        }

        // Calculate number of nights
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (nights <= 0) {
            throw new RuntimeException("Check-out date must be after check-in date!");
        }

        // Calculate total price
        Double totalPrice = dormitory.getPrice() * nights;

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setDormitory(dormitory);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setTotalPrice(totalPrice);
        booking.setStatus("CONFIRMED");
        booking.setPaymentStatus("PENDING");
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        // Mark bed as unavailable
        dormitory.setIsAvailable(false);
        dormitoryRepository.save(dormitory);

        // Save booking
        return bookingRepository.save(booking);
    }

    // Get all bookings for a user
    public List<Booking> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    // Get all bookings (for admin/owner)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Apply coupon/discount to booking
    public Booking applyDiscount(Long bookingId, Double discountAmount) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }

        // Check if discount is more than total price
        if (discountAmount > booking.getTotalPrice()) {
            throw new RuntimeException("Discount cannot be more than total price!");
        }

        booking.setDiscountApplied(discountAmount);
        booking.setTotalPrice(booking.getTotalPrice() - discountAmount);
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    // Mark booking as paid
    public Booking markAsPaid(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }

        booking.setPaymentStatus("PAID");
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    // Cancel booking
    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }

        // Mark room/bed as available again
        if (booking.getRoom() != null) {
            Room room = booking.getRoom();
            room.setIsAvailable(true);
            roomRepository.save(room);
        }

        if (booking.getDormitory() != null) {
            Dormitory dormitory = booking.getDormitory();
            dormitory.setIsAvailable(true);
            dormitoryRepository.save(dormitory);
        }

        booking.setStatus("CANCELLED");
        booking.setUpdatedAt(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    // Get daily bookings (for owner dashboard)
    public List<Booking> getTodaysBookings() {
        List<Booking> allBookings = bookingRepository.findAll();
        // Filter for today's bookings
        return allBookings.stream()
                .filter(b -> b.getCheckInDate().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .toList();
    }
}