package com.evan.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity@Table(name = "gameType")
public class GameType {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer typeId;
	private String typeName;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "gametypeList",
			joinColumns = {@JoinColumn(name = "TYPEID")},
			inverseJoinColumns= {@JoinColumn(name = "GAMEID")})
	private Set<Game> games = new HashSet<>();
	
	public GameType() {}

	public GameType(String typeName, Set<Game> games) {
		super();
		this.typeName = typeName;
		this.games = games;
	}

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

	public Set<Game> getGames() {
		return games;
	}

	public void setGames(Set<Game> games) {
		this.games = games;
	}

	
}
