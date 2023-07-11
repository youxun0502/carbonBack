package com.ni.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ItemLogRepository extends JpaRepository<ItemLog, Integer> {

//	================ 查詢此會員每個道具的最新1筆log ================
	@Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY itemId, memberId ORDER BY id DESC) sn "
			+ "FROM itemLog WHERE memberId = :id ) r WHERE r.sn = 1 AND total >= 1", 
			nativeQuery = true)
	public List<ItemLog> findByMemberId(@Param("id") Integer id);

//	================ 查詢此會員的最新1筆log ================
	@Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY itemId, memberId ORDER BY id DESC) sn "
			+ "FROM itemLog WHERE memberId = :memberId ) r WHERE r.sn = 1 AND itemId = :itemId", 
			nativeQuery = true)
	public ItemLog findByMemberIdAndItemId(@Param("memberId") Integer memberId, @Param("itemId") Integer itemId);
	
//	================ 查詢最新5筆log紀錄 ================
	@Query(value = "SELECT TOP 5 * FROM itemLog WHERE memberId = :id ORDER BY id DESC", nativeQuery = true)
	public List<ItemLog> findOrderHistory(@Param("id") Integer id);
}
