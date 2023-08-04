package com.ni.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
//	================ 查詢會員log紀錄 新=>舊 ================
	@Query("FROM ItemLog WHERE memberId = :id AND ordId IS NOT NULL ORDER BY id DESC")
	public Page<ItemLog> findOrderHistory(@Param("id") Integer id, Pageable page);
}
