package com.liao.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

public interface MessagesReposity extends JpaRepository<Messages, Integer> {

	@Query("FROM Messages m WHERE m.userName LIKE %:userName%")
	public List<Messages> findMessagesByUserName(@PathVariable("userName") String userName);
	
	@Query("FROM Messages m WHERE m.mtitle LIKE :mtitle")
	public List<Messages> findMessagesByTitle(@PathVariable("mtitle") String mtitle);
	
	@Query("FROM Messages m WHERE m.gameName LIKE %:gameName%")
	public List<Messages> findMessagesByGameName(@PathVariable("gameName") String gameName);
	
	
	
}
