package com.ecom.service;

import java.util.List;
import java.util.Optional;

import com.ecom.model.Cart;
import com.ecom.model.Coupon;
import com.ecom.model.User;

public interface CouponService {
    Cart applyCoupon(String code, double orderValue, User user) throws Exception;
    Cart removeCoupon(String code, User user) throws Exception;
    Coupon createCoupon(Coupon coupon);
    void deleteCoupon(Long couponId);
    List<Coupon> getAllCoupons();
    
    Coupon getCouponById(Long couponId);
}
