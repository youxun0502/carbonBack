package com.chen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chen.model.Competition;
import com.chen.service.CompetitionService;
import com.evan.dao.GameRepository;
import com.evan.model.Game;

@Controller
public class CompetitionController {

	@Autowired
	private CompetitionService cService;
	
	@Autowired
	private GameRepository gRepo;
	
	//跳轉新增頁面
	@GetMapping("/competition/add")
	public String processInsert(Model m) {
		List<Game> gameList = gRepo.findAll();
		m.addAttribute("gameList", gameList);
		return "chen/insertCompetition";
	}
	
	//跳轉單筆頁面
	@GetMapping("/competition/update")
	public String processUpdate(@RequestParam("competitionId")Integer id,Model m) {
		Competition competition = cService.findById(id);
		m.addAttribute("competition", competition);
		return "chen/updateCompetition";
	}
	
	//查詢全部
	@GetMapping("/competition/data")
	public String findALL(Model m) {
		List<Competition> competitiionList = cService.findAll();
		m.addAttribute("competitionList", competitiionList);
		return "chen/competitionData";
	}
	
	//透過賽事名稱搜尋
	@PostMapping("/competition/data")
	public String findDataByName(@RequestParam("gameName")String gameName,Model m) {
		List<Competition> competitionList = cService.findByName(gameName);
		m.addAttribute("competitionList", competitionList);
		return "chen/competitionData";
	}
	
	//新增資料
	@PostMapping("/competition/insert")
	public String insertData(@RequestParam("gameId")Integer gameId,@RequestParam("name")String name,@RequestParam("mode")String mode,
							@RequestParam(value = "desc", required = false)String desc,@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,
							@RequestParam("location")String location,@RequestParam(value = "prize", required = false)Integer prize,@RequestParam("quotaLimited")Integer quotaLimited,
							@RequestParam("deadline")String deadline) {
		Competition comp = new Competition();
		comp.setGameId(gameId);
		comp.setName(name);
		comp.setMode(mode);
		comp.setDescription(desc);
		comp.setStartDate(startDate);
		comp.setEndDate(endDate);
		comp.setLocation(location);
		comp.setPrize(prize);
		comp.setQuotaLimited(quotaLimited);
		comp.setDeadline(deadline);
		cService.insert(comp);
		return "redirect:/competition/data";
	}
	
	//修改資料
	@PutMapping("/competition/update")
	public String updatePost(@RequestParam("competitionId")Integer id,@RequestParam("name")String name,@RequestParam("mode")String mode,
							@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,@RequestParam("location")String location,
							@RequestParam(value = "prize", required = false)Integer prize,@RequestParam("quotaLimited")Integer quotaLimited,@RequestParam("deadline")String deadline) {
		cService.updateCompetitionById(id, name, mode, startDate, endDate, location, prize, quotaLimited, deadline);
		return "redirect:/competition/data";
	}
	
	//刪除資料
	@DeleteMapping("/competition/delete")
	public String deletePost(@RequestParam("competitionId")Integer id) {
		cService.deleteById(id);
		return "redirect:/competition/data";
	}
	
}
