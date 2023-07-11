package com.evan.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evan.dao.GameRepository;
import com.evan.dto.GameDTO;
import com.evan.model.Game;
import com.evan.utils.ConvertToDTO;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gRepo;
	@Autowired
	private ConvertToDTO ctDTO;
	
	//------------------抓全部--------------------
	public List<GameDTO> getAllGameInfo() {return ctDTO.outputGameDTOList(gRepo.findAll());}
	
	//------------------id查--------------------
	public Game getGameInfoById(Integer id) {
		return gRepo.findById(id).isPresent() ? gRepo.findById(id).get() : null;
	}
	
	//------------------新建遊戲-------------------
	public Game insertGameInfo(Game data) {
		return gRepo.save(data);}
	
	//------------------修改遊戲-------------------

	@Transactional
	public Game updateGameInfo(Game data) {
		Optional<Game> game = gRepo.findById(data.getGameId());
		System.out.println(data);
		if(game.isPresent()) {
			System.out.println(game);
			Game g = game.get();
			if(data.getBuyerCount()!= null)g.setBuyerCount(data.getBuyerCount());
			if(data.getCreateDate()!= null)g.setCreateDate(data.getCreateDate());
			if(data.getGameIntroduce()!= null)g.setGameIntroduce(data.getGameIntroduce());
			if(data.getGameTypes().size()!=0)g.setGameTypes(data.getGameTypes());
			if(data.getGameName()!= null)g.setGameName(data.getGameName());
			if(data.getPrice()!= null)g.setPrice(data.getPrice());
			if(data.getStatus()!= null)g.setStatus(data.getStatus());
			System.out.println(g);
			return g;
		}
		System.out.println("no update data ");
		return null;
	}
	
	//------------------名字模糊-------------------
	public List<GameDTO> SearchLikeName(String gName) {
		return ctDTO.outputGameDTOList(gRepo.SearchLikeName(gName));
	}
	
	//------------------名字相同-------------------
	public boolean SearchUserByName(String userName) {
		return !gRepo.SearchUserByName(userName).isEmpty();
	}
	
	//------------------刪除ById-------------------
	@Transactional
	public boolean deleteGame(Integer gameId) {
		boolean exist = gRepo.existsById(gameId);
		if (exist) {
			gRepo.deleteById(gameId);
			return true;
		}
		return false;
	}

	public List<GameDTO> getGameInfoByGameName(String gameName) {
		List<GameDTO> gameDTOS = gRepo.SearchUserByName(gameName)!=null?ctDTO.outputGameDTOList(gRepo.SearchUserByName(gameName)):null;
		return gameDTOS;
	}

	public List<GameDTO> SearchPrice(String minValue, String maxValue) {
		List<Game> findPrice = gRepo.findGameByPriceBetween(Integer.parseInt(minValue),Integer.parseInt(maxValue));
		List<GameDTO> gameDTOS = findPrice!=null?ctDTO.outputGameDTOList(findPrice):null;
		return gameDTOS;
	}

}
