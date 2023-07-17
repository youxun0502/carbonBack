package com.liu.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

	@Query("from Chat where (fromMember = :from or fromMember = :to) AND (toMember = :to or toMember = :from) order by chattingRoomLogId DESC")
	public Page<Chat> findLastTenChat(@Param("from") Integer from, @Param("to") Integer to, Pageable pgb);

	@Query("from Chat where fromMember = :from and toMember = :to and isRead = 2")
	public List<Chat> findNoReadChat(@Param("to")Integer to, @Param("from")Integer from);

	@Query(value = "select count(*) notRead from chattingRoomLog where fromMember =:from AND isRead = 2", nativeQuery = true)
	public Integer findNotReadByMemberId(@Param("from") Integer from);
}
