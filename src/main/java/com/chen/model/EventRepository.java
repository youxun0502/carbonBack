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

	
}
