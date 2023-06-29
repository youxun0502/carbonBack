package com.liu.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liu.model.Coupon;
import com.liu.model.CouponLog;
import com.liu.model.CouponLogRepository;
import com.liu.model.CouponRepository;
import com.liu.model.Member;

@Service
public class CouponLogService {

	@Autowired
	CouponLogRepository couponLogRepository;
	
	@Autowired
	CouponRepository couponRepository;
	
	public String insertCouponLog(CouponLog couponLog) {
	CouponLog lastCouponLog= couponLogRepository.findFirstByOrderByAcquisitionDateDesc(couponLog.getMemberId());
	System.out.println(lastCouponLog);
		if(lastCouponLog != null) {
			System.out.println(lastCouponLog.getAcquisitionDate());
			System.out.println(new Date());
			System.out.println(lastCouponLog.getAcquisitionDate().compareTo(new Date()));
			if(lastCouponLog.getAcquisitionDate().compareTo(new Date())!=0) {
				Optional<Coupon> coupon = couponRepository.findById(1);
				if(coupon.isPresent()) {
					couponLog.setCouponId(coupon.get().getCouponId());
					System.out.println("錯誤1");
					couponLogRepository.save(couponLog);
					return coupon.get().getDesc();
					
				}else {
					return null;
				}
			}else {
				return "您今日已參加過抽獎了!!!";
			}
		}else {
			Optional<Coupon> coupon = couponRepository.findById(1);
			if(coupon.isPresent()) {				
				couponLog.setCoupon(coupon.get());
				couponLog.setStatus(1);
				System.out.println("--------------------------");
				couponLogRepository.save(couponLog);
				return coupon.get().getDesc();
			}else {
				return null;
			}
		}
	}
}
