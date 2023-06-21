package com.evan.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.evan.dao.GameTypeRepository;
import com.evan.dto.GameDTO;
import com.evan.dto.TypeDTO;
import com.evan.model.Game;
import com.evan.model.GameType;
@Component
public class ConvertToDTO {
	
	@Autowired
	private GameTypeRepository gtRepo;
	
	public List<GameDTO> outputGameDTOList(List<Game> games) {
		
		ArrayList<GameDTO> gameDTOList = new ArrayList<>();

		for (Game game : games) {
			GameDTO gameDTO = new GameDTO();
			if(game.getGameId()!= null ) gameDTO.setGameId(game.getGameId());
			if(game.getGameName()!= null ) gameDTO.setGameName(game.getGameName());
			if(game.getGameIntroduce()!= null ) gameDTO.setGameIntroduce(game.getGameIntroduce());
			if(game.getGamePhotoLists()!= null ) gameDTO.setGamePhotoLists(game.getGamePhotoLists());
			if(game.getGameTypes()!= null ) gameDTO.setGameTypes(game.getGameTypes());
			if(game.getCreateDate()!= null ) gameDTO.setCreateDate(game.getCreateDate());
			if(game.getBuyerCount()!= null ) gameDTO.setBuyerCount(game.getBuyerCount());
			if(game.getPrice()!= null ) gameDTO.setPrice(game.getPrice());
			if(game.getStatus()!= null ) gameDTO.setStatus(game.getStatus());
			gameDTOList.add(gameDTO);
		}
		return gameDTOList;
	}

	public List<TypeDTO> outputTypeDTOList(List<GameType> findAll) {
		ArrayList<TypeDTO> typeDTOList = new ArrayList<>();

		for (GameType Type : findAll) {
			TypeDTO typeDTO = new TypeDTO();
			if(Type.getTypeId()!= null ) {

				if(!Type.getTypeName().equals("") && Type.getGames().size()!= 0) {
					typeDTO.setTypeId(Type.getTypeId());
					if(Type.getTypeName()!= null ) typeDTO.setTypeName(Type.getTypeName());
					if(Type.getGames()!= null ) typeDTO.setGames(Type.getGames());
					typeDTOList.add(typeDTO);
				}else {				
					gtRepo.deleteById(Type.getTypeId());
				}
			}
		}
		return typeDTOList;
	}




}
