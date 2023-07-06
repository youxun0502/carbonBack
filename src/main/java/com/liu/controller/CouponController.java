package com.liu.controller;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.config.MathRandom;
import com.liu.dto.CouponDto;
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
		return "/liu/couponGetCoupon";
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
	
	@GetMapping("/coupon/couponManagementPage")
	public String couponManagementPage(Model m) {
		
		Map<Integer, Float> couponRandoms = couponService.getCouponRamdomForManagement();
		
		List<Coupon> coupons = couponService.findCouponOrderByCouponId();
		
		System.out.println(couponRandoms.get(1));
		
		m.addAttribute("couponRandoms", couponRandoms);
		m.addAttribute("coupons", coupons);
		return "/liu/couponManagement";
	}
	
	@ResponseBody
	@GetMapping("/coupon/api/couponMangement")
	public List<CouponDto> couponMangement(){
	Map<Integer, Float> couponRandoms = couponService.getCouponRamdomForManagement();
	List<Coupon> coupons = couponService.findCouponOrderByCouponId();
	List<CouponDto> couponDtos = new ArrayList<>();
	for (Coupon coupon : coupons) {
		CouponDto couponDto = new CouponDto();
		couponDto.setCouponId(coupon.getCouponId());
		couponDto.setCouponName(coupon.getDesc());
		couponDto.setRandom(couponRandoms.get(coupon.getCouponId()));
		couponDtos.add(couponDto);
	}
	
	return couponDtos;
	}
		
	
}
