package com.evan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.evan.model.GamePhoto;

public interface GamePhotoRepository extends JpaRepository<GamePhoto, Integer> {

	//----------------已gameId找尋photoList--------
	@Query("from GamePhoto where gameId = :id")
	List<GamePhoto> searchPhotoIdByGameId(@Param("id") int id);


}
