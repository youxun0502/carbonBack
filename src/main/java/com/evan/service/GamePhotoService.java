package com.evan.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.evan.dao.GamePhotoRepository;
import com.evan.dao.GameRepository;
import com.evan.model.Game;
import com.evan.model.GamePhoto;

@Service
public class GamePhotoService {

	@Autowired
	private GamePhotoRepository gpRepo;
	@Autowired
	private GameRepository gRepo;

	// ------------------撈圖ById-------------------
	public GamePhoto getPhotoById(Integer id) {
		Optional<GamePhoto> optional = gpRepo.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// ------------------找圖片Id依賴遊戲Id-------------------
	public List<Integer> searchPhotoIdByGameId(int id) {
		List<Integer> idList = new ArrayList<>();
		List<GamePhoto> photoList = gpRepo.searchPhotoIdByGameId(id);
		if (!photoList.isEmpty()) {
			for (GamePhoto gamePhoto : photoList) {
				idList.add(gamePhoto.getPhotoId());
			}
			return idList;
		}
		return null;
	}

	// ------------------刪除ById-------------------
	@Transactional
	public boolean deleteGamePhoto(Integer id) {
		boolean exist = gpRepo.existsById(id);
		if (exist) {
			gpRepo.deleteById(id);
			return true;
		}
		return false;
	}

	// -----------------新增圖片---------------------
	public GamePhoto addImg(Integer gameId, MultipartFile file) {
		Optional<Game> game = gRepo.findById(gameId);
		GamePhoto gamePhoto = new GamePhoto();
		try {
			if (game.isPresent()) {
				gamePhoto.setPhotoFile(file.getBytes());
				gamePhoto.setGameId(gameId);
				gamePhoto.setGame(game.get());
				return gpRepo.save(gamePhoto);
			}else {
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
}
