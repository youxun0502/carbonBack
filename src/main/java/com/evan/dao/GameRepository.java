package com.evan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evan.model.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {

	// -----------------名字模糊--------------
	@Query("from Game where gameName like %:n%")
	public List<Game> SearchLikeName(@Param("n") String name);

	// -----------------名字一樣--------------
	@Query("from Game where gameName = :name")
	public List<Game> SearchUserByName(@Param("name") String name);

	@Query("SELECT g FROM Game g JOIN g.gameTypes t1 JOIN g.gameTypes t2 WHERE t1.typeName = :typeA AND t2.typeName = :typeB")
	public List<Game> findGamesByTypes(@Param("typeA") String typeA, @Param("typeB") String typeB);
	
	@Query("SELECT g FROM Game g JOIN g.gameTypes t1 JOIN g.gameTypes t2 JOIN g.gameTypes t3 WHERE t1.typeName = :typeA AND t2.typeName = :typeB AND t3.typeName = :typeC")
	public List<Game> findGamesByTypes(@Param("typeA") String typeA, @Param("typeB") String typeB, @Param("typeC") String typeC);

	List<Game> findByGameTypesTypeName(String typeName);

	public List<Game> findGameByGameName(String gameName);

	public List<Game> findGameByPriceBetween(int parseInt, int parseInt2);


}
