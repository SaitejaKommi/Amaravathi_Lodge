package com.amaravathi.demo.service;

import com.amaravathi.demo.model.Review;
import com.amaravathi.demo.model.User;
import com.amaravathi.demo.model.Room;
import com.amaravathi.demo.model.Dormitory;
import com.amaravathi.demo.model.Booking;
import com.amaravathi.demo.repository.ReviewRepository;
import com.amaravathi.demo.repository.UserRepository;
import com.amaravathi.demo.repository.RoomRepository;
import com.amaravathi.demo.repository.DormitoryRepository;
import com.amaravathi.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private DormitoryRepository dormitoryRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Create review for a room
    public Review createRoomReview(Long userId, Long roomId, Long bookingId, Integer rating, String title, String comment) {

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

        // Validate booking exists
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }

        // Validate rating is 1-5
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5!");
        }

        // Validate title and comment are not empty
        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException("Review title is required!");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new RuntimeException("Review comment is required!");
        }

        // Create review
        Review review = new Review();
        review.setUser(user);
        review.setRoom(room);
        review.setBooking(booking);
        review.setRating(rating);
        review.setTitle(title);
        review.setComment(comment);
        review.setIsVerifiedBooking(true);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // Create review for a dormitory
    public Review createDormitoryReview(Long userId, Long dormitoryId, Long bookingId, Integer rating, String title, String comment) {

        // Validate user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        // Validate dormitory exists
        Dormitory dormitory = dormitoryRepository.findById(dormitoryId).orElse(null);
        if (dormitory == null) {
            throw new RuntimeException("Dormitory not found!");
        }

        // Validate booking exists
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            throw new RuntimeException("Booking not found!");
        }

        // Validate rating is 1-5
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5!");
        }

        // Validate title and comment are not empty
        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException("Review title is required!");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new RuntimeException("Review comment is required!");
        }

        // Create review
        Review review = new Review();
        review.setUser(user);
        review.setDormitory(dormitory);
        review.setBooking(booking);
        review.setRating(rating);
        review.setTitle(title);
        review.setComment(comment);
        review.setIsVerifiedBooking(true);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // Get all reviews for a room
    public List<Review> getRoomReviews(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            throw new RuntimeException("Room not found!");
        }
        return reviewRepository.findByRoom(room);
    }

    // Get all reviews for a dormitory
    public List<Review> getDormitoryReviews(Long dormitoryId) {
        Dormitory dormitory = dormitoryRepository.findById(dormitoryId).orElse(null);
        if (dormitory == null) {
            throw new RuntimeException("Dormitory not found!");
        }
        return reviewRepository.findByDormitory(dormitory);
    }

    // Get average rating for a room
    public Double getAverageRoomRating(Long roomId) {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (room == null) {
            throw new RuntimeException("Room not found!");
        }

        List<Review> reviews = reviewRepository.findByRoom(room);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        Double sum = reviews.stream()
                .mapToDouble(Review::getRating)
                .sum();

        return sum / reviews.size();
    }

    // Get average rating for a dormitory
    public Double getAverageDormitoryRating(Long dormitoryId) {
        Dormitory dormitory = dormitoryRepository.findById(dormitoryId).orElse(null);
        if (dormitory == null) {
            throw new RuntimeException("Dormitory not found!");
        }

        List<Review> reviews = reviewRepository.findByDormitory(dormitory);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        Double sum = reviews.stream()
                .mapToDouble(Review::getRating)
                .sum();

        return sum / reviews.size();
    }

    // Get review by ID
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }

    // Get all reviews by a user
    public List<Review> getUserReviews(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }
        return reviewRepository.findByUser(user);
    }

    // Owner reply to review
    public Review replyToReview(Long reviewId, String reply) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) {
            throw new RuntimeException("Review not found!");
        }

        if (reply == null || reply.trim().isEmpty()) {
            throw new RuntimeException("Reply cannot be empty!");
        }

        review.setOwnerReply(reply);
        review.setReplyDate(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // Delete review (user can delete own review)
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}