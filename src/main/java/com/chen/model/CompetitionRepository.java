package com.chen.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompetitionRepository extends JpaRepository<Competition, Integer> {

	@Query("from Competition where name like %:name%")
	public List<Competition> findByNamelike(String name);
	
	public Page<Competition> findByGameId(Integer gameId,Pageable pgb);
	
	@Query("from Competition order by startDate asc")
	public List<Competition> findAllOrderByStartDate();
}
