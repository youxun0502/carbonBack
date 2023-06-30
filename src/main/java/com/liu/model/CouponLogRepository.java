package com.liu.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponLogRepository extends JpaRepository<CouponLog, Integer> {


	@Query(nativeQuery = true, value = "select top 1 * from couponLog where memberId =:memberId  Order By  acquisitionDate DESC;")
	public  CouponLog findFirstByMemberIdOrderByAcquisitionDateDesc(@Param("memberId") Integer memberId);

	
	
}
