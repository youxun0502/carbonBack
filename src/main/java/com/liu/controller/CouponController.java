package com.liu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CouponController {

	@GetMapping("/coupon/couponPage")
	public String couponPage() {
		return "liu/getCoupon";
	}
	
//	@ResponseBody
//	@GetMapping("/coupon/api/getCoupon")
//	public String getCoupon(@RequestParam(name = "memberId") int memberId) {
//		return "liu/getCoupon";
//	}
}
