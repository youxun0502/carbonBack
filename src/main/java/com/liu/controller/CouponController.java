package com.liu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CouponController {

	@GetMapping("/coupon/couponPage")
	public String CuponPage() {
		return "liu/getCoupon";
	}
}
