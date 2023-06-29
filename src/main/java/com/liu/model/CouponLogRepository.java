package com.liu.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponLogRepository extends JpaRepository<CouponLog, Integer> {

	@Query("SELECT c FROM CouponLog c WHERE c.memberId = :id ORDER BY c.acquisitionDate DESC")
	public  CouponLog findFirstByOrderByAcquisitionDateDesc(@Param("id") Integer id);
	
	
}
