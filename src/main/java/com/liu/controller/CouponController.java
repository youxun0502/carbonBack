package com.liu.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.config.MathRandom;
import com.liu.model.Coupon;
import com.liu.model.CouponLog;
import com.liu.model.Member;
import com.liu.service.CouponLogService;
import com.liu.service.CouponService;
import com.liu.service.MemberService;

@Controller
public class CouponController {
	
	@Autowired
	MemberService mService;
	
	@Autowired
	CouponLogService couponLogService;
	
	@Autowired
	CouponService couponService;
	
	@Autowired
	MathRandom mathRandom;

	@GetMapping("/coupon/couponPage")
	public String couponPage() {
		return "liu/couponGetCoupon";
	}
	
	@ResponseBody
	@GetMapping("/coupon/api/getCoupon")
	public String getCoupon(@RequestParam(name = "memberId") int memberId) {
		
		int random = 1 + mathRandom.random().nextInt(9999);
		
		Coupon  coupon = couponService.getCouponByRandom(random);
		Member member = mService.findById(memberId);
		System.out.println("controller +"+ coupon.getCouponId());
		CouponLog couponLog = new CouponLog();
		couponLog.setMember(member);
		couponLog.setCoupon(coupon);
		String result = couponLogService.insertCouponLog(couponLog);
		return result;
	}
}
