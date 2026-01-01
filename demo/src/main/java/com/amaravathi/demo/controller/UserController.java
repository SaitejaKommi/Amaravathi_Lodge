package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.User;
import com.amaravathi.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // API ENDPOINT 1: Register a new user
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(
                user.getEmail(),
                user.getPhone(),
                user.getName()
        );
    }

    // API ENDPOINT 2: Get user by ID
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // API ENDPOINT 3: Get user by email
    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // API ENDPOINT 4: Apply referral bonus
    @PostMapping("/{userId}/apply-referral")
    public String applyReferral(@PathVariable Long userId,
                                @RequestParam Double bonusAmount) {
        userService.applyReferralBonus(userId, bonusAmount);
        return "Referral bonus of " + bonusAmount + "rs applied!";
    }
}