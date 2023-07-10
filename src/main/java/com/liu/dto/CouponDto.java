package com.liu.dto;

public class CouponDto {

	public CouponDto() {}

	private Integer couponId;
	
	private String couponName;
	
	private Float random;
	
	private String typeName;
	
	private Float discount;
	
	private Integer weight;
	
	private Integer status;
	
	private Integer updateInteger; 
	
	public Boolean getIsAll() {
		return isAll;
	}

	public void setIsAll(Boolean isAll) {
		this.isAll = isAll;
	}

	private Boolean isAll;

	public Integer getCouponId() {
		return couponId;
	}

	public Integer getUpdateInteger() {
		return updateInteger;
	}

	public void setUpdateInteger(Integer updaInteger) {
		this.updateInteger = updaInteger;
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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	
	
}
