package com.evan.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.evan.model.GameType;
public interface GameTypeRepository extends JpaRepository<GameType,Integer> {

	public GameType getGameTypeByTypeName(String type);

	

}
