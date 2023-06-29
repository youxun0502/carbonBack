package com.evan.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.evan.dto.GameDTO;
import com.evan.service.GameTypeService;
@Component
public class GetInfoToGameFront {
	
	public GetInfoToGameFront() {
	}

	@Autowired
	private GameTypeService gtService ;

	public List<GameDTO> getRandomGames(String typeString, String gameName) {
		String[] typeList = typeString.split(",");
	    List<GameDTO> selectedGames = new ArrayList<>();
	    Set<String> selectedNames = new HashSet<>();
	    selectedNames.add(gameName);
	    
	    for (String type : typeList) {
	    	System.out.println(type);
	    	List<GameDTO> games = gtService.findGameByTypeName(type);
	    	System.out.println(games);
	    	for (GameDTO game : games) {
	            if (selectedGames.size() == 6) {
	                return selectedGames;
	            }
	            if (!selectedNames.contains(game.getGameName())) {
	                selectedGames.add(game);
	                selectedNames.add(game.getGameName());
	            }
	        }
	    }
	    
	    return selectedGames;
	}
	
}
