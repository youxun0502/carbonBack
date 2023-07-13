package com.evan.dto;

import org.springframework.beans.factory.annotation.Autowired;

import com.evan.dao.GameRepository;
import com.evan.model.GamePhoto;

import lombok.Data;

@Data
public class OrderLogDTO {

	private String gameName;
	
	private Integer price;
	
	private Integer photoId;
	
	@Autowired
	private GameRepository gRepos;

	public Integer getPhotoId() {
		
		GamePhoto gamePhoto = gRepos.findGameByGameName(gameName).get(0).getGamePhotoLists().get(0);
		photoId =gamePhoto.getPhotoId();
		return photoId;
	}

	
	
}
