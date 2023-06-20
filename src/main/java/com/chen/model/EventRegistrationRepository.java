package com.chen.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Integer> {

	@Query("from EventRegistration where realName like %:realName%")
	public List<EventRegistration> findByNamelike(String realName);
}
