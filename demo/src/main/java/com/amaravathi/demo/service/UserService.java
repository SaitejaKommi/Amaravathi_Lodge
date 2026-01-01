package com.amaravathi.demo.service;

import com.amaravathi.demo.model.User;
import com.amaravathi.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Business Logic: Register a new user
    public User registerUser(String email, String phone, String name) {

        // RULE 1: Check if email is empty
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required!");
        }

        // RULE 2: Check if user already exists
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            throw new RuntimeException("User already registered with this email!");
        }

        // RULE 3: Create new user
        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setName(name);
        user.setCreatedAt(LocalDateTime.now());
        user.setWalletBalance(0.0);

        // Save to database
        return userRepository.save(user);
    }

    // Business Logic: Get user by ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Business Logic: Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Business Logic: Apply referral bonus
    public void applyReferralBonus(Long userId, Double bonusAmount) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.setWalletBalance(user.getWalletBalance() + bonusAmount);
            userRepository.save(user);
        }
    }
}