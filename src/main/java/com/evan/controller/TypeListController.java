package com.evan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.dto.GameDTO;
import com.evan.dto.TypeDTO;
import com.evan.service.GameTypeService;
import com.evan.utils.SortChartJs;

@Controller
public class TypeListController {

	// --------------------注入--------------------------
	@Autowired
	private GameTypeService gtService;
	@Autowired
	private SortChartJs sortChartJs;
	
	//====================跳轉主頁===========================
	@Transactional
	@GetMapping("/game/type")
	public String TypeMain() {
		return "evan/TagAllList";
	}
	
	@GetMapping("/game/type/all")
	public String TypeList(Model model) {
		List<TypeDTO> allTypeInfo = gtService.getAllTypeInfo();
		sortChartJs.init(allTypeInfo, 1);
		model.addAttribute("typeList",allTypeInfo);
		model.addAttribute("sort",sortChartJs);
		return "evan/TagAllList";
	}
	
	// ----------------sortAjex
	@Transactional
	@GetMapping("/game/type/sort")
	@ResponseBody
	public SortChartJs TypeSort(@RequestParam Map<String, Object> formData) {
		int choose = Integer.parseInt((String) formData.get("choose"));
		List<TypeDTO> allTypeInfo = gtService.getAllTypeInfo();
		System.out.println(allTypeInfo);
		sortChartJs.init(allTypeInfo, choose);
		return sortChartJs;
	}
	// ----------------tpyeNameList Ajax
	@Transactional
	@GetMapping("/game/type/findGameListByType")
	@ResponseBody
	public List<GameDTO> findGameByType(@RequestParam Map<String, Object> formData) {
		List<GameDTO> gameList = gtService.findGameByTypeName((String)formData.get("typeName"));
		
		return gameList;
	}
	
	

	

	
}
