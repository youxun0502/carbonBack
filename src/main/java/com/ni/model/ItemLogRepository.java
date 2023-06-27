package com.ni.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemLogRepository extends JpaRepository<ItemLog, Integer> {

	@Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY itemId, memberId ORDER BY id DESC) sn "
			+ "FROM itemLog WHERE memberId = id ) r WHERE r.sn = 1", 
			nativeQuery = true)
	public List<ItemLog> findByMemberId(Integer id);

	@Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY itemId, memberId ORDER BY id DESC) sn "
			+ "FROM itemLog WHERE memberId = :memberId ) r WHERE r.sn = 1 AND itemId = :itemId", 
			nativeQuery = true)
	public ItemLog findByMemberIdAndItemId(Integer memberId, Integer itemId);
}
