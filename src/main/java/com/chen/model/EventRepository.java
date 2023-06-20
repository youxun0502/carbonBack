package com.chen.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("from Event where name like %:name%")
	public List<Event> findByNamelike(String name);
}
