package com.liu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	@Query("Select SUM(weight) from Coupon where status = 1")
	public Integer getTotalWeight();
	
	@Query("from Coupon Order By couponId")
	public List<Coupon> findCouponOrderByCouponId();
	
	@Query("from Coupon  where status =1 Order By couponId")
	public List<Coupon> findCouponWhereStatusNotEqualOneOrderByCouponId();
}
