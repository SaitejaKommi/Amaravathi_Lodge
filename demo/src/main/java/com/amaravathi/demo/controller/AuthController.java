package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.User;
import com.amaravathi.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Login endpoint
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String phone = loginData.get("phone");

        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required!");
        }

        // Find user by email
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found! Please register first.");
        }

        // Return user data
        return Map.of(
                "success", true,
                "id", user.getId(),
                "email", user.getEmail(),
                "name", user.getName(),
                "phone", user.getPhone(),
                "message", "Login successful!"
        );
    }
}