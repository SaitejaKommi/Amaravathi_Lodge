package com.amaravathi.demo.repository;

import com.amaravathi.demo.model.Review;
import com.amaravathi.demo.model.Room;
import com.amaravathi.demo.model.Dormitory;
import com.amaravathi.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByRoom(Room room); // Get all reviews for a room
    List<Review> findByDormitory(Dormitory dormitory); // Get all reviews for a dormitory
    List<Review> findByUser(User user); // Get all reviews by a user
    List<Review> findByRoomAndRating(Room room, Integer rating); // Get reviews by rating
    List<Review> findByDormitoryAndRating(Dormitory dormitory, Integer rating); // Get reviews by rating
}