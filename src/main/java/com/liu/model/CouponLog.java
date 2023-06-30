package com.liu.model;

import java.util.Calendar;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
	
	@Temporal(TemporalType.DATE)
	@Column(name = "acquisitionDate")
	private Date acquisitionDate;
	
	@Temporal(TemporalType.DATE)
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
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	@JoinColumn(name = "couponId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Coupon coupon;
	
	public Integer getCouponLogId() {
		return couponLogId;
	}

	public void setCouponLogId(Integer couponLogId) {
		this.couponLogId = couponLogId;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public Date getAcquisitionDate() {
		return acquisitionDate;
	}

	public void setAcquisitionDate(Date acquisitionDate) {
		this.acquisitionDate = acquisitionDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public CouponLog() {}

}
