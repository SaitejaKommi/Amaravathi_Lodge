package com.amaravathi.demo.repository;

import com.amaravathi.demo.model.Coupon;
import com.amaravathi.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Coupon findByCouponCode(String couponCode); // Find by coupon code
    List<Coupon> findByIsActive(Boolean isActive); // Get active coupons
    List<Coupon> findByCouponType(String couponType); // Get coupons by type
    List<Coupon> findByReferredByUser(User user); // Get coupons generated for a user
}