package com.chen.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompetitionRegistrationRepository extends JpaRepository<CompetitionRegistration, Integer> {

	@Query("from CompetitionRegistration where realName like %:realName%")
	public List<CompetitionRegistration> findByNamelike(String realName);
}
