package com.li.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BonusPointLogRepository extends JpaRepository<BonusPointLog, Integer> {
	@Query(value = "select top 1 * from bonuspointlog where memberId = :memberId order by logId desc", nativeQuery = true)
	public BonusPointLog theLastPoint(@Param("memberId") Integer memberId);
	@Query(value = "select * from bonuspointlog where memberId = :memberId order by logId desc", nativeQuery = true)
	public List<BonusPointLog> findByMemberId(@Param("memberId") Integer memberId);

}
