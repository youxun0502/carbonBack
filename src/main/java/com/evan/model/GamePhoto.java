package com.evan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity@Table(name = "gamePhoto")
public class GamePhoto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int photoId;
	@Lob
	private byte[] photoFile;
	
	@Column(name = "gameId",insertable=false, updatable=false)
	private int gameId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gameId")
	private Game game;
	
	public GamePhoto() {}
	
	public int getPhotoId() {
		return photoId;
	}
	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}
	public byte[] getPhotoFile() {
		return photoFile;
	}
	public void setPhotoFile(byte[] photoFile) {
		this.photoFile = photoFile;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	
}
