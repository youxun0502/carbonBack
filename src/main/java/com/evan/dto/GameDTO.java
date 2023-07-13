package com.evan.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.evan.model.GamePhoto;
import com.evan.model.GameType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Data
public class GameDTO {


	private Integer gameId;
	private String gameName;
	private Integer price;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createDate;
	private String gameIntroduce;
	private Integer buyerCount;
	private Integer status;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date today;
	
	private List<GamePhoto> gamePhotoLists= new ArrayList<>();
//	
	private Set<GameType> gameTypes = new HashSet<>();
	
	public List<Integer> getGamePhotoLists() {
		List<Integer> photoIdList = new ArrayList<>();
		for (GamePhoto photo : gamePhotoLists) {
			photoIdList.add(photo.getPhotoId()) ;
		}
		return photoIdList;
	}
	
	public void setGamePhotoLists(List<GamePhoto> gamePhotoLists) {
		this.gamePhotoLists = gamePhotoLists;
	}
	
	public String getGameTypes() {
		String typeNameList ="";
		
		for (GameType type : gameTypes) {
			typeNameList += typeNameList.equals("")?type.getTypeName():(","+type.getTypeName());
		}
		return typeNameList;
	}
	
	public void setGameTypes(Set<GameType> gameTypes) {
		this.gameTypes = gameTypes;
	}
	
	public Date getToday() {
		return new Date();
	}

	
	
}
