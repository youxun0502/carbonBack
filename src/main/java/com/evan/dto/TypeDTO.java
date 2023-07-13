package com.evan.dto;

import java.util.HashSet;
import java.util.Set;

import com.evan.model.Game;
import com.fasterxml.jackson.annotation.JsonIgnore;


public class TypeDTO {

	private Integer typeId;
	private String typeName;
	@JsonIgnore
	private Set<Game> games = new HashSet<>();
	
	private Integer totalSalesRev ;
	private Integer gameNum;
	private Integer typeBuyer;

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public void setGames(Set<Game> games) {
		this.games = games;
	}

	public Integer getGameNum() {
		gameNum = games.size();
		return gameNum;
	}

	public Integer getTotalSalesRev() {
		totalSalesRev = (Integer) 0;
		for (Game game : games) {
//			System.out.println(game.getPrice());
			totalSalesRev += game.getPrice()*game.getBuyerCount();
		}
		return totalSalesRev;
	}

	public Integer getTypeBuyer() {
		typeBuyer = 0;
		for (Game game : games) {
			typeBuyer += game.getBuyerCount();
		}
		return typeBuyer;
	}

}
