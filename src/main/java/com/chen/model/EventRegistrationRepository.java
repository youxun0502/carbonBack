package com.chen.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Integer> {

	@Query("from EventRegistration where realName like %:realName%")
	public List<EventRegistration> findByNamelike(String realName);
	
	@Query("from Event where gameId = :gameId order by startDate asc")
	public List<Event> findByGameId(Integer gameId);
	
	@Query("from EventRegistration where eventId = :eventId")
	public List<EventRegistration> findByEventId(Integer eventId);
	
	@Query("from EventRegistration where memberId = :memberId")
	public List<EventRegistration> findMemberRecord(Integer memberId);
	
}
