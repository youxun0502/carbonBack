package com.evan.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.chen.model.Competition;
import com.chen.model.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liu.model.Member;
import com.ni.model.GameItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity@Table(name = "game")
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameId;
	private String gameName;
	private Integer price;
	private Date createDate;
	private String gameIntroduce;
	private Integer buyerCount;
	private Integer status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "game", cascade = CascadeType.ALL)
	private List<GamePhoto> gamePhotoLists= new ArrayList<>();
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "gametypelist", 
		joinColumns = {@JoinColumn(name = "GAMEID")},
		inverseJoinColumns = {@JoinColumn(name = "TYPEID")})
	private Set<GameType> gameTypes = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "membercart", 
	    joinColumns = {@JoinColumn(name = "GAMEID")},
	    inverseJoinColumns = {@JoinColumn(name = "MEMBERID")})
	private Set<Member> members = new HashSet<>();

	
	public Game() {}
	
	@JsonIgnore
	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
	private List<Competition> competitions = new ArrayList<>();
	
	@JsonIgnore
	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
	private List<Event> event = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
	private List<GameItem> gameItem = new ArrayList<>();
	

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", gameName=" + gameName + ", price=" + price + ", createDate="
				+ createDate + ", gameIntroduce=" + gameIntroduce + ", buyerCount="
				+ buyerCount + ", status=" + status + ", gamePhotoLists=" + gamePhotoLists + ", gameTypes=" + gameTypes + "]";
	}

	public Integer getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getGameIntroduce() {
		return gameIntroduce;
	}
	public void setGameIntroduce(String gameIntroduce) {
		this.gameIntroduce = gameIntroduce;
	}

	public Integer getBuyerCount() {
		return buyerCount;
	}
	public void setBuyerCount(int buyerCount) {
		this.buyerCount = buyerCount;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public List<GamePhoto> getGamePhotoLists() {
		return gamePhotoLists;
	}

	public void setGamePhotoLists(List<GamePhoto> gamePhotoLists) {
		this.gamePhotoLists = gamePhotoLists;
	}

	public Set<GameType> getGameTypes() {
		return gameTypes;
	}

	public void setGameTypes(Set<GameType> gameTypes) {
		this.gameTypes = gameTypes;
	}
	
}
