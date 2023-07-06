package com.liu.dto;

public class CouponDto {

	public CouponDto() {}

	private Integer couponId;
	
	private String couponName;
	
	private Float random;

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Float getRandom() {
		return random;
	}

	public void setRandom(Float random) {
		this.random = random;
	}
	
	
}
