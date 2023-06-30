package com.evan.utils;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.evan.dao.GameTypeRepository;
import com.evan.model.Game;
import com.evan.model.GamePhoto;
import com.evan.model.GameType;
@Component
public class GetInfoFromReq {
	@Autowired
	private GameTypeRepository gtRepo;

	// --------------更新、創建區
	public  Game getInfoFromRequest(Map<String, Object> formData, MultipartFile[] files) {

		Game game = new Game();
		List<GamePhoto> gamePhotoList = new ArrayList<>();
		HashSet<GameType> typeList = new HashSet<>();

		try {
			// Id 處理-------------------------------------
			if (formData.get("GameId") != null)game.setGameId(Integer.parseInt((String) formData.get("GameId")));

			// 資料處理---------------------------------------------
			if (formData.get("GameName") != null)game.setGameName((String) formData.get("GameName"));
			if (formData.get("Price") != null)game.setPrice(Integer.parseInt((String) formData.get("Price")));
			if (formData.get("CreateDate") != null)game.setCreateDate(Date.valueOf((String) formData.get("CreateDate")));
			if (formData.get("GameIntroduce") != null)game.setGameIntroduce((String) formData.get("GameIntroduce"));
			if (formData.get("BuyerCount") != null)game.setBuyerCount(Integer.parseInt((String) formData.get("BuyerCount")));
			if (formData.get("Status") != null)game.setStatus(Integer.parseInt((String) formData.get("Status")));
			System.out.println(game);

			// 圖片資料處理-------------------------------------------------
			if (files != null && files.length != 0) {
				for (MultipartFile file : files) {
					GamePhoto gamePhoto = new GamePhoto();
					gamePhoto.setPhotoFile(file.getBytes());
					gamePhoto.setGameId(0);
					gamePhoto.setGame(game);
					gamePhotoList.add(gamePhoto);
				}
				game.setGamePhotoLists(gamePhotoList);
			}

			// 遊戲種類---------------------------------------------------------
			if (formData.get("GameTypes") != null) {

				System.out.println("test");
				HashSet<Game> gameSet = new HashSet<>();
				gameSet.add(game);
				String[] stringList = ((String) formData.get("GameTypes")).split(",");
				System.out.println(stringList);
				
				// 使用 HashSet 過濾重複值
			    HashSet<String> uniqueTypes = new HashSet<>(Arrays.asList(stringList));
				
			    // 刪除遊戲目前擁有的所有種類標籤
			    game.getGameTypes().clear();
			    
				for (String type : uniqueTypes) {
					// 檢查種類名稱是否已存在
					GameType existingType = gtRepo.getGameTypeByTypeName(type);
					if (existingType != null) {
						// 若已存在，直接使用現有的種類
						game.getGameTypes().add(existingType);
					} else {
						// 若不存在，則新增種類
						GameType gametype = new GameType();
						gametype.setTypeName(type);
						typeList.add(gametype);
						game.getGameTypes().add(gametype);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(game);
		return game;
	}
}
