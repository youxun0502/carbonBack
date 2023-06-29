package com.liu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.model.CouponLog;
import com.liu.model.Member;
import com.liu.service.CouponLogService;
import com.liu.service.MemberService;

@Controller
public class CouponController {
	
	@Autowired
	MemberService mService;
	
	@Autowired
	CouponLogService couponLogService;

	@GetMapping("/coupon/couponPage")
	public String couponPage() {
		return "liu/getCoupon";
	}
	
	@ResponseBody
	@GetMapping("/coupon/api/getCoupon")
	public String getCoupon(@RequestParam(name = "memberId") int memberId) {
		
		Member member = mService.findById(memberId);
		CouponLog couponLog = new CouponLog();
		couponLog.setMember(member);
		String result = couponLogService.insertCouponLog(couponLog);
		return result;
	}
}
