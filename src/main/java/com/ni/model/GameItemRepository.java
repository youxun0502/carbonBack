package com.ni.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evan.model.Game;

public interface GameItemRepository extends JpaRepository<GameItem, Integer> {

	@Query("FROM GameItem WHERE itemName LIKE %:name%")
	public List<GameItem> findByNameLike(@Param("name") String name);
	
	@Query("FROM Game")
	public List<Game> findGameName();
	
	@Query("SELECT COUNT(*) itemName FROM GameItem WHERE itemName = :name AND gameId= :id")
	public int checkItemName(@Param("name") String name, @Param("id") Integer id);
	
	@Query("FROM GameItem WHERE gameId = :id OR itemName LIKE %:name%")
	public List<GameItem> findByNameOrGame(@Param("id") Integer gameId, @Param("name") String itemName);
	
	@Query("FROM GameItem WHERE gameId = :id AND itemName LIKE %:name%")
	public List<GameItem> findByNameAndGame(@Param("id") Integer gameId, @Param("name") String itemName);
}
