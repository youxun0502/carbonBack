package com.liu.model;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "couponLog")
public class CouponLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "couponLogId")
	private Integer couponLogId;
	
	@Column(name = "memberId", insertable = false, updatable = false)
	private Integer memberId;
	
	@Column(name = "couponId", insertable = false, updatable = false)
	private Integer couponId;
	
	@Column(name = "acquisitionDate")
	private Date acquisitionDate;
		
	@Column(name="expirationDate")
	private Date expirationDate;
		
	@PrePersist
	public void onCreate() {
		if(acquisitionDate == null) {
			Date date = new Date();
			acquisitionDate = date;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			Date dateAfterSevenDay = calendar.getTime();
			expirationDate = dateAfterSevenDay;
		}
	}
	
	@Column(name = "[status]")
	private Integer status;
	
	@JoinColumn(name = "memberId")
	@ManyToOne
	private Member member;
	
	@JoinColumn(name = "couponId")
	@ManyToOne
	private Coupon coupon;
	
	public CouponLog() {}

}
