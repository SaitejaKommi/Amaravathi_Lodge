package com.amaravathi.demo.service;

import com.amaravathi.demo.model.Coupon;
import com.amaravathi.demo.model.User;
import com.amaravathi.demo.repository.CouponRepository;
import com.amaravathi.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserRepository userRepository;

    // Create coupon for first booking (50rs discount)
    public Coupon createFirstBookingCoupon(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        String couponCode = "FIRST50-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponCode);
        coupon.setDiscountAmount(50.0);
        coupon.setCouponType("FIRST_BOOKING");
        coupon.setMaxUsageCount(1); // Can use only once
        coupon.setUsageCount(0);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(30)); // Valid for 30 days
        coupon.setIsActive(true);
        coupon.setDescription("50rs discount on first booking");
        coupon.setReferredByUser(user);
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setUpdatedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    // Create referral coupon for referred user (150rs discount on first booking)
    public Coupon createReferralCouponForReferee(Long refereeUserId) {
        User refereeUser = userRepository.findById(refereeUserId).orElse(null);
        if (refereeUser == null) {
            throw new RuntimeException("Referral user not found!");
        }

        String couponCode = "REFER150-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponCode);
        coupon.setDiscountAmount(150.0); // Referee gets 150rs
        coupon.setCouponType("REFERRAL");
        coupon.setMaxUsageCount(1); // Can use only once
        coupon.setUsageCount(0);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(30)); // Valid for 30 days
        coupon.setIsActive(true);
        coupon.setDescription("150rs discount - Referral bonus for new user");
        coupon.setReferredByUser(refereeUser);
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setUpdatedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    // Create coupon for referrer (100rs added to wallet)
    public Coupon createReferralCouponForReferrer(Long referrerUserId) {
        User referrerUser = userRepository.findById(referrerUserId).orElse(null);
        if (referrerUser == null) {
            throw new RuntimeException("Referrer user not found!");
        }

        String couponCode = "REFER100-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Coupon coupon = new Coupon();
        coupon.setCouponCode(couponCode);
        coupon.setDiscountAmount(100.0); // Referrer gets 100rs
        coupon.setCouponType("REFERRAL");
        coupon.setMaxUsageCount(999); // Can use unlimited times
        coupon.setUsageCount(0);
        coupon.setExpiryDate(LocalDateTime.now().plusDays(365)); // Valid for 1 year
        coupon.setIsActive(true);
        coupon.setDescription("100rs discount - Referral bonus for each successful referral");
        coupon.setReferredByUser(referrerUser);
        coupon.setCreatedAt(LocalDateTime.now());
        coupon.setUpdatedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    // Validate and get coupon
    public Coupon validateCoupon(String couponCode) {
        Coupon coupon = couponRepository.findByCouponCode(couponCode);

        if (coupon == null) {
            throw new RuntimeException("Coupon not found!");
        }

        if (!coupon.getIsActive()) {
            throw new RuntimeException("Coupon is not active!");
        }

        if (coupon.getUsageCount() >= coupon.getMaxUsageCount()) {
            throw new RuntimeException("Coupon usage limit exceeded!");
        }

        if (LocalDateTime.now().isAfter(coupon.getExpiryDate())) {
            throw new RuntimeException("Coupon has expired!");
        }

        return coupon;
    }

    // Use coupon (increment usage count)
    public Coupon useCoupon(String couponCode) {
        Coupon coupon = validateCoupon(couponCode);

        coupon.setUsageCount(coupon.getUsageCount() + 1);
        coupon.setUpdatedAt(LocalDateTime.now());

        // Deactivate if usage limit reached
        if (coupon.getUsageCount() >= coupon.getMaxUsageCount()) {
            coupon.setIsActive(false);
        }

        return couponRepository.save(coupon);
    }

    // Get coupon by code
    public Coupon getCouponByCode(String couponCode) {
        return couponRepository.findByCouponCode(couponCode);
    }

    // Get coupon by ID
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id).orElse(null);
    }

    // Get all active coupons
    public List<Coupon> getActiveCoupons() {
        return couponRepository.findByIsActive(true);
    }

    // Get coupons by type
    public List<Coupon> getCouponsByType(String couponType) {
        return couponRepository.findByIsActive(true);
    }

    // Get user's coupons
    public List<Coupon> getUserCoupons(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        return couponRepository.findByReferredByUser(user);
    }

    // Deactivate coupon (admin/owner action)
    public Coupon deactivateCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElse(null);
        if (coupon == null) {
            throw new RuntimeException("Coupon not found!");
        }

        coupon.setIsActive(false);
        coupon.setUpdatedAt(LocalDateTime.now());

        return couponRepository.save(coupon);
    }

    // Get coupon discount amount
    public Double getCouponDiscount(String couponCode) {
        Coupon coupon = validateCoupon(couponCode);
        return coupon.getDiscountAmount();
    }
}