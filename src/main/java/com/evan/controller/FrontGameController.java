package com.evan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.evan.utils.SortChartJs;

@Controller
public class FrontGameController {

	@Autowired 
	private GameService gService;
	@Autowired
	private GameTypeService gtService;
	@Autowired
	private SortChartJs sortChartJs;
	
	@GetMapping("/gameFront")
	public String GameMain() {
		return "evan/SingleGamePage";
	}
	
	//-------------------新建照片
	@GetMapping("/gameFront/{gameName}")
//	@ResponseBody
	public String addImg(@PathVariable String gameName,Model model) {
		GameDTO gameDTO = gService.getGameInfoByGameName(gameName).get(0);
		List<TypeDTO> allTypeInfo = gtService.getAllTypeInfo();
		sortChartJs.sortAll(allTypeInfo);
		System.out.println(gameDTO);
		model.addAttribute("gameDTO",gameDTO);
		model.addAttribute("typeList",sortChartJs.getTypeList());
//		return gameDTO;
		return "evan/SingleGamePage";
	}
	
}
