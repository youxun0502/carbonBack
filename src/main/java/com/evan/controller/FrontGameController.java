package com.evan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.evan.dao.GameRepository;
import com.evan.dto.GameDTO;
import com.evan.dto.TypeDTO;
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
	
	@GetMapping("/gameFront")
	public String GameList(Model model) {
		sortChartJs.sortGameDTOAll(gService.getAllGameInfo());
		model.addAttribute("gameAll",gService.getAllGameInfo());
		model.addAttribute("gameList",sortChartJs.getGameList());
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
	
	
	
}
