package com.ni.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evan.model.Game;
import com.ni.model.GameItem;
import com.ni.model.GameItemRepository;

@Service
public class GameItemService {
	
	@Autowired
	private GameItemRepository itemRepo;

	public List<GameItem> findAll() {
		return itemRepo.findAll();
	}
	
	public Page<GameItem> findAllByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 9);
		return itemRepo.findAll(pgb);
	}
	
	public List<GameItem> findByLikeName(String itemName) {
		return itemRepo.findByNameLike(itemName);
	}
	
	public List<GameItem> findByNameOrGame(Integer gameId, String itemName) {
		return itemRepo.findByNameOrGame(gameId, itemName);
	}
	
	public List<GameItem> findByNameAndGame(Integer gameId, String itemName) {
		return itemRepo.findByNameAndGame(gameId, itemName);
	}
	
	public GameItem findById(Integer itemId) {
		Optional<GameItem> optional = itemRepo.findById(itemId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Transactional
	public GameItem updateById(GameItem gameItem) {
		Optional<GameItem> optional = itemRepo.findById(gameItem.getItemId());
		if(optional.isPresent()) {
			GameItem item = optional.get();
			item.setItemName(gameItem.getItemName());
			item.setGameId(gameItem.getGameId());
			item.setItemDesc(gameItem.getItemDesc());
			if(gameItem.getItemImgName() != null) item.setItemImgName(gameItem.getItemImgName());
			if(gameItem.getItemImg() != null) item.setItemImg(gameItem.getItemImg());
			item.setItemGrade(gameItem.getItemGrade());
			item.setItemType(gameItem.getItemType());
			item.setStatus(gameItem.getStatus());
			item.setGame(gameItem.getGame());
			return item;
		}
		System.out.println("no update data");
		return null;
	}
	
	public GameItem insert(GameItem item) {
		return itemRepo.save(item);
	}
	
	public void delete(Integer itemId) {
		itemRepo.deleteById(itemId);
		return ;
	}
	
	public boolean checkItemName(String itemName, Integer itemId) {
		int count = itemRepo.checkItemName(itemName, itemId);
		return count > 0;
	}
	
	public List<Game> findGameName() {
		return itemRepo.findGameName();
	}
}
