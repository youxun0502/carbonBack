package com.liu.config;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class RandomConfig {

	private Integer totalWeight = 10000;
	
	private Map<Integer,Integer> couponRandom;

	public Integer getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Integer totalWeight) {
		this.totalWeight = totalWeight;
	}

	public Map<Integer, Integer> getCouponRandom() {
		return couponRandom;
	}

	public void setCouponRandom(Map<Integer, Integer> couponRandom) {
		this.couponRandom = couponRandom;
	}
}
