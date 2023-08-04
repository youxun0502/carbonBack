package com.evan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.dto.GameDTO;
import com.evan.dto.OrderLogDTO;
import com.evan.dto.TypeDTO;
import com.evan.service.GameOrderService;
import com.evan.service.GameService;
import com.evan.service.GameTypeService;
import com.evan.utils.GetInfoToGameFront;
import com.evan.utils.SortChartJs;

@Controller
public class FrontGameController {

	@Autowired 
	private GameService gService;
	@Autowired
	private GameTypeService gtService;
	@Autowired
	private SortChartJs sortChartJs;
	@Autowired
	private GetInfoToGameFront gifToGameFront;
	@Autowired
	private GameOrderService goService;
	
	@GetMapping("/gameFront")
	public String GameList(Model model) {
		List<TypeDTO> allTypeInfo = gtService.getAllTypeInfo();
		sortChartJs.sortAll(allTypeInfo);
		sortChartJs.sortGameDTOAll(gService.getAllGameInfo());
		model.addAttribute("popularTag",sortChartJs.getTypeList());
		model.addAttribute("gameAll",gService.getAllGameInfo());
		model.addAttribute("gameList",sortChartJs.getGameList());
		System.out.println(sortChartJs.getTypeList());
		return "evan/GameList";
	}
	
	//-------------------單個遊戲頁面
	@GetMapping("/gameFront/{gameName}")
	@Transactional
//	@ResponseBody
	public String addImg(@PathVariable String gameName,Model model) {
		GameDTO gameDTO = gService.getGameInfoByGameName(gameName).get(0);
		List<TypeDTO> allTypeInfo = gtService.getAllTypeInfo();
		sortChartJs.sortAll(allTypeInfo);
		sortChartJs.sortGameDTOAll(gService.getAllGameInfo());
		
		System.out.println(gameDTO);
		model.addAttribute("gameDTO",gameDTO);
		model.addAttribute("gameList",sortChartJs.getGameList());
		model.addAttribute("typeList",sortChartJs.getTypeList());
		model.addAttribute("sixGames",gifToGameFront.getRandomGames(gameDTO.getGameTypes(),gameName));
		
//		return gameDTO;
		return "evan/SingleGamePage";
	}
	
	@GetMapping("/gameFront/getownGame")
	@ResponseBody
	public List<OrderLogDTO> getMemberOwnGames(@RequestParam Map<String, Object> formData,Model model){
		System.out.println("hello");
		return goService.getMemberOwnGames(formData);
	}
	
	// ----------------tpyeNameList Ajax
	@Transactional
	@GetMapping("/gameFront/findGameByType")
	@ResponseBody
	public List<GameDTO> findGameByType(@RequestParam Map<String, Object> formData) {
		List<GameDTO> gameList = gtService.findGameByTypeName((String)formData.get("typeName"));
		return gameList;
	}
	
	//模糊查詢遊戲名字
	@ResponseBody
	@GetMapping("/gameFront/getGameLikesName")
	public List<GameDTO> SearchLikeName(@RequestParam Map<String, Object> formData) {
		List<GameDTO> gamesInfos = gService.SearchLikeName((String) formData.get("gameName"));
		System.out.println(gamesInfos);
		return gamesInfos;
	}
	
	//以價格查詢遊戲
	@ResponseBody
	@GetMapping("/gameFront/findPrice")
	public List<GameDTO> SearchPrice(@RequestParam Map<String, Object> formData) {
		List<GameDTO> gamesInfos = gService.SearchPrice((String) formData.get("minValue"),(String) formData.get("maxValue"));
		System.out.println(gamesInfos);
		return gamesInfos;
	}

	
	
}
