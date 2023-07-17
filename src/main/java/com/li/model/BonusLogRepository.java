package com.li.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BonusLogRepository extends JpaRepository<BonusLog, Integer> {
	
	@Query(value = "select * from bonuslog where memberId = :memberId ", nativeQuery = true)
	public List<BonusLog> findByMemberId(@Param("memberId") Integer memberId);
	@Query(value = "select * from bonuslog where memberId = :memberId AND bonusId = :bonusId ", nativeQuery = true)
	public List<BonusLog> findByMemberIdAndBonusId(@Param("memberId") Integer memberId,@Param("bonusId") Integer bonusId);

}
