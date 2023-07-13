package com.liao.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

public interface DiscussionsReposity extends JpaRepository<Discussions, Integer> {
	
	@Query("FROM Discussions d WHERE d.gameName LIKE %:gameName%")
	public List<Discussions> findDiscussionsByGameName(@PathVariable("gameName") String gameName);

	
	@Query("FROM Discussions d WHERE d.userName LIKE %:userName%")
	public List<Discussions> findDiscussionsByUserName(@PathVariable("userName") String userName);
	
	@Query("FROM Discussions d WHERE d.title LIKE %:title%")
	public List<Discussions> findDiscussionsByTitle(@PathVariable("title") String title);

	

}


