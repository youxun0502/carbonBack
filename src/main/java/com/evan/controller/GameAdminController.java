package com.evan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.evan.dto.GameDTO;
import com.evan.model.GamePhoto;
import com.evan.service.GamePhotoService;
import com.evan.service.GameService;
import com.evan.utils.GetInfoFromReq;

import jakarta.servlet.http.HttpSession;

@Controller
public class GameAdminController {

	// --------------------注入--------------------------
	@Autowired
	private GameService gService;
	@Autowired
	private GamePhotoService gpService;
	@Autowired
	private GetInfoFromReq getInfoFromReq;
	// --------------------call game主頁-----------------------
	@GetMapping("/game")
	public String GameMain() {
		return "evan/GameDashBoardHome";
	}

	// -------------------全部遊戲
	@GetMapping("/game/getAllGames")
	@ResponseBody
	public List<GameDTO> GetAllGamesInfos() {
		List<GameDTO> gamesInfos = gService.getAllGameInfo();
		System.out.println(gamesInfos);
		return gamesInfos;
	}

	//-------------------新建照片
	@PostMapping("/game/addImg/{gameId}")
	@ResponseBody
	public List<Integer> addImg(@PathVariable Integer gameId,
			@RequestParam("file") MultipartFile file) {
		gpService.addImg(gameId,file);
		List<Integer> photoIdLists = gpService.searchPhotoIdByGameId(gameId);
		return photoIdLists;
	}

	// ------------------新建遊戲
	@PostMapping("/game/create")
	public String createGame(@RequestParam Map<String, Object> formData,
			@RequestParam("gameImg") MultipartFile[] files) {
		gService.insertGameInfo(getInfoFromReq.getInfoFromRequest(formData, files));
		return "evan/GameDashBoardHome";
	}

	// --------------以名字模糊查詢遊戲
	@ResponseBody
	@GetMapping("/game/getGameLikesName")
	public List<GameDTO> SearchLikeName(@RequestParam Map<String, Object> formData) {
		List<GameDTO> gamesInfos = gService.SearchLikeName((String) formData.get("gameName"));
		System.out.println(gamesInfos);
		return gamesInfos;
	}

	// -----------------Ajex查找重複名字
	@ResponseBody
	@GetMapping("/game/searchSameName")
	public boolean SearchUserName(@RequestParam Map<String, Object> formData) {
		String userName = (String) formData.get("username");
		boolean flag = gService.SearchUserByName(userName);
		return flag;
	}


	// ----------------遊戲id找相片id
	@ResponseBody
	@GetMapping("/game/getPhotoIdByGameId")
	public List<Integer> findPhotoIdByGameId(@RequestParam Map<String, Object> formData,HttpSession session) {
		int id = Integer.parseInt((String) formData.get("GameId"));
		List<Integer> photoIdLists = gpService.searchPhotoIdByGameId(id);
		return photoIdLists;
	}

	// ----------------刪除遊戲
	@DeleteMapping("/game/delete")
	public String deleteGame(@RequestParam Map<String, Object> formData) {
		int id = Integer.parseInt((String) formData.get("id"));
		gService.deleteGame(id);
		return "evan/GameDashBoardHome";
	}

	// ----------------刪除遊戲
	@DeleteMapping("/game/deleteImg/{id}/{gameId}")
	@ResponseBody
	public List<Integer> deleteGame(@PathVariable Integer id,@PathVariable Integer gameId) {
		gpService.deleteGamePhoto(id);
		List<Integer> photoIdLists = gpService.searchPhotoIdByGameId(gameId);
		return photoIdLists;
	}

	// ---------------更新遊戲
	@PutMapping("/game/updateNoImg")
	public String updateUserByIdNoImg(@RequestParam Map<String, Object> formData) {
		MultipartFile[] files = null;
		gService.updateGameInfo(getInfoFromReq.getInfoFromRequest(formData, files));
		return "evan/GameDashBoardHome";
	}

	// ---------------圖片controller建置
	@GetMapping("gameFront/getImg/{id}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable Integer id) {
		GamePhoto photo1 = gpService.getPhotoById(id);
		byte[] pImg = photo1.getPhotoFile();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		// 檔案,header,Httpstatus
		return new ResponseEntity<byte[]>(pImg, headers, HttpStatus.OK);
	}


}
