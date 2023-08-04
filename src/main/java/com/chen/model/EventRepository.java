package com.chen.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("from Event where name like %:name%")
	public List<Event> findByNamelike(String name);
	
	public Page<Event> findByGameId(Integer gameId,Pageable pgb);
	
	@Query("from Event order by startDate asc")
	public List<Event> findAllOrderByStartDate();
	
	@Query(value="SELECT e.eventId,e.name,e.description,e.location,e.startDate,e.endDate,e.deadline,e.fee,e.gameId,e.photo,e.quotaLimited,e.timeLimitedDiscount "
			+ "FROM event e  JOIN eventRegistration er "
			+ "ON e.eventId = er.eventId "
			+ "WHERE er.memberId = :memberId and e.name like CONCAT('%',:eventName,'%')",
			nativeQuery = true)
	public List<Event> findMemberRecord(Integer memberId,String eventName);

	
}
