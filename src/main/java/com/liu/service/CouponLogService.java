package com.liu.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.liu.model.CouponLog;
import com.liu.model.CouponLogRepository;
import com.liu.model.CouponRepository;

@Service
public class CouponLogService {

	@Autowired
	CouponLogRepository couponLogRepository;

	@Autowired
	CouponRepository couponRepository;

	public String insertCouponLog(CouponLog couponLog) {

		CouponLog lastCouponLog = couponLogRepository
				.findFirstByMemberIdOrderByAcquisitionDateDesc(couponLog.getMember().getId());
		if (lastCouponLog != null) {

			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
			String newDate = outputFormat.format(new Date()); // 今天日期
			String acquisitionDate = outputFormat.format(lastCouponLog.getAcquisitionDate()); // 資料庫最新一筆日期
			if (!newDate.equals(acquisitionDate)) {
				couponLog.setStatus(1);
				couponLogRepository.save(couponLog);
				return couponLog.getCoupon().getDesc();
			} else {
				return "您今日已參加過抽獎了!!!";
			}
		} else {
				couponLog.setStatus(1);
				couponLogRepository.save(couponLog);
				return couponLog.getCoupon().getDesc();
			}
		}
	}
