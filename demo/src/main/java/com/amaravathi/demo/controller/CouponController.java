package com.amaravathi.demo.controller;

import com.amaravathi.demo.model.Coupon;
import com.amaravathi.demo.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    // Create first booking coupon for a user
    @PostMapping("/first-booking/{userId}")
    public Coupon createFirstBookingCoupon(@PathVariable Long userId) {
        return couponService.createFirstBookingCoupon(userId);
    }

    // Create referral coupon for referee (person being referred)
    @PostMapping("/referral/referee/{userId}")
    public Coupon createReferralCouponForReferee(@PathVariable Long userId) {
        return couponService.createReferralCouponForReferee(userId);
    }

    // Create referral coupon for referrer (person who referred)
    @PostMapping("/referral/referrer/{userId}")
    public Coupon createReferralCouponForReferrer(@PathVariable Long userId) {
        return couponService.createReferralCouponForReferrer(userId);
    }

    // Validate coupon
    @GetMapping("/validate/{couponCode}")
    public Map<String, Object> validateCoupon(@PathVariable String couponCode) {
        try {
            Coupon coupon = couponService.validateCoupon(couponCode);
            return Map.of(
                    "isValid", true,
                    "couponCode", coupon.getCouponCode(),
                    "discountAmount", coupon.getDiscountAmount(),
                    "expiryDate", coupon.getExpiryDate()
            );
        } catch (RuntimeException e) {
            return Map.of(
                    "isValid", false,
                    "message", e.getMessage()
            );
        }
    }

    // Use coupon (apply coupon)
    @PostMapping("/use/{couponCode}")
    public Map<String, Object> useCoupon(@PathVariable String couponCode) {
        Coupon coupon = couponService.useCoupon(couponCode);
        return Map.of(
                "success", true,
                "couponCode", coupon.getCouponCode(),
                "discountAmount", coupon.getDiscountAmount(),
                "usageCount", coupon.getUsageCount(),
                "maxUsageCount", coupon.getMaxUsageCount()
        );
    }

    // Get coupon by code
    @GetMapping("/code/{couponCode}")
    public Coupon getCouponByCode(@PathVariable String couponCode) {
        return couponService.getCouponByCode(couponCode);
    }

    // Get coupon by ID
    @GetMapping("/{id}")
    public Coupon getCouponById(@PathVariable Long id) {
        return couponService.getCouponById(id);
    }

    // Get all active coupons
    @GetMapping("/active")
    public List<Coupon> getActiveCoupons() {
        return couponService.getActiveCoupons();
    }

    // Get coupons by type
    @GetMapping("/type/{couponType}")
    public List<Coupon> getCouponsByType(@PathVariable String couponType) {
        return couponService.getCouponsByType(couponType);
    }

    // Get user's coupons
    @GetMapping("/user/{userId}")
    public List<Coupon> getUserCoupons(@PathVariable Long userId) {
        return couponService.getUserCoupons(userId);
    }

    // Get coupon discount amount
    @GetMapping("/{couponCode}/discount")
    public Map<String, Object> getCouponDiscount(@PathVariable String couponCode) {
        try {
            Double discount = couponService.getCouponDiscount(couponCode);
            return Map.of(
                    "couponCode", couponCode,
                    "discountAmount", discount
            );
        } catch (RuntimeException e) {
            return Map.of(
                    "error", e.getMessage()
            );
        }
    }

    // Deactivate coupon (admin/owner)
    @PostMapping("/{couponId}/deactivate")
    public Coupon deactivateCoupon(@PathVariable Long couponId) {
        return couponService.deactivateCoupon(couponId);
    }
}