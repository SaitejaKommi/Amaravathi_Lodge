package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Review;
import com.amaravathi.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Create review for a room
    @PostMapping("/room/create")
    public Review createRoomReview(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        Long roomId = Long.parseLong(request.get("roomId").toString());
        Long bookingId = Long.parseLong(request.get("bookingId").toString());
        Integer rating = Integer.parseInt(request.get("rating").toString());
        String title = request.get("title").toString();
        String comment = request.get("comment").toString();

        return reviewService.createRoomReview(userId, roomId, bookingId, rating, title, comment);
    }

    // Create review for a dormitory
    @PostMapping("/dormitory/create")
    public Review createDormitoryReview(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        Long dormitoryId = Long.parseLong(request.get("dormitoryId").toString());
        Long bookingId = Long.parseLong(request.get("bookingId").toString());
        Integer rating = Integer.parseInt(request.get("rating").toString());
        String title = request.get("title").toString();
        String comment = request.get("comment").toString();

        return reviewService.createDormitoryReview(userId, dormitoryId, bookingId, rating, title, comment);
    }

    // Get all reviews for a room
    @GetMapping("/room/{roomId}")
    public List<Review> getRoomReviews(@PathVariable Long roomId) {
        return reviewService.getRoomReviews(roomId);
    }

    // Get all reviews for a dormitory
    @GetMapping("/dormitory/{dormitoryId}")
    public List<Review> getDormitoryReviews(@PathVariable Long dormitoryId) {
        return reviewService.getDormitoryReviews(dormitoryId);
    }

    // Get average rating for a room
    @GetMapping("/room/{roomId}/rating")
    public Map<String, Object> getAverageRoomRating(@PathVariable Long roomId) {
        Double avgRating = reviewService.getAverageRoomRating(roomId);
        return Map.of(
                "roomId", roomId,
                "averageRating", avgRating
        );
    }

    // Get average rating for a dormitory
    @GetMapping("/dormitory/{dormitoryId}/rating")
    public Map<String, Object> getAverageDormitoryRating(@PathVariable Long dormitoryId) {
        Double avgRating = reviewService.getAverageDormitoryRating(dormitoryId);
        return Map.of(
                "dormitoryId", dormitoryId,
                "averageRating", avgRating
        );
    }

    // Get review by ID
    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    // Get all reviews by a user
    @GetMapping("/user/{userId}")
    public List<Review> getUserReviews(@PathVariable Long userId) {
        return reviewService.getUserReviews(userId);
    }

    // Owner reply to review
    @PostMapping("/{reviewId}/reply")
    public Review replyToReview(
            @PathVariable Long reviewId,
            @RequestParam String reply) {
        return reviewService.replyToReview(reviewId, reply);
    }

    // Delete review
    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return "Review deleted successfully!";
    }
}