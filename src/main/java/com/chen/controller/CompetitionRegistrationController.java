package com.chen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chen.model.Competition;
import com.chen.model.CompetitionRegistration;
import com.chen.model.CompetitionRepository;
import com.chen.model.EventRegistration;
import com.chen.service.CompetitionRegistrationService;
import com.chen.service.CompetitionService;
import com.evan.dao.GameRepository;
import com.evan.model.Game;
import com.liu.model.Member;
import com.liu.service.MemberService;

@Controller
public class CompetitionRegistrationController {

	@Autowired
	private CompetitionRegistrationService crService;

	@Autowired
	private CompetitionRepository cRepo;
	
	@Autowired
	private GameRepository gRepo;
	
	@Autowired
	private CompetitionService cService;
	
	@Autowired
	private MemberService mService;
	
	//////////    前台管理    //////////
	
	// 跳轉賽事總覽頁面
	@GetMapping("/competitionPageAll")
	public String eventFrontPageAll(@RequestParam(name="p", defaultValue = "1") Integer pageNumber, Model m) {
		List<Game> games = gRepo.findAll();
		m.addAttribute("games", games);
		
		Page<Competition> page = crService.findByPageAll(pageNumber);
		m.addAttribute("page", page);
		return "chen/competitionFrontPageAll";
	}
	
	// 跳轉賽事分類頁面
	@GetMapping("/competitionPageOne")
	public String eventFrontPageOne(@RequestParam(name="p", defaultValue = "1") Integer pageNumber,@RequestParam(value = "gameId", required = false)Integer gameId,Model m) {
		List<Game> games = gRepo.findAll();
		m.addAttribute("games", games);
		
		Page<Competition> page = crService.findByPageOne(pageNumber,gameId);
		m.addAttribute("page", page);
		return "chen/competitionFrontPageOne";
	}
	
	// 跳轉賽事細節頁面
	@GetMapping("/competitionPageDetail")
	public String eventFrontPageDetail(@RequestParam("competitionId")Integer competitionId,Model m) {
		Competition competition = cService.findById(competitionId);
		m.addAttribute("competition", competition);
		return "chen/competitionFrontPageDetail";
	}

	// 跳轉新增頁面
	@GetMapping("/competitionRegistration")
	public String signupPage(@RequestParam("competitionId")Integer competitionId,@RequestParam(value = "memberId", required = false)Integer memberId,Model m) {
		
		if(memberId != null) {
			Member member = mService.findById(memberId);
			m.addAttribute("member", member);
		}
		
		Competition competition = cService.findById(competitionId);
		m.addAttribute("competition", competition);
		return "chen/competitionRegistration";
	}

	// 新增資料
	@PostMapping("/competitionRegistration/insert")
	public String inserData(@RequestParam("competitionId") Integer id,
			@RequestParam("gameNickname") String gameNickname,@RequestParam(value = "memberId", required = false) Integer memberId,
			@RequestParam(value = "teamName", required = false) String teamName,
			@RequestParam("realName") String realName, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam(value = "address", required = false) String address) {
		CompetitionRegistration cr = new CompetitionRegistration();
		cr.setCompetitionId(id);
		cr.setGameNickname(gameNickname);
		cr.setTeamName(teamName);
		cr.setRealName(realName);
		cr.setEmail(email);
		cr.setPhone(phone);
		cr.setAddress(address);
		cr.setMemberId(memberId);
		crService.insert(cr);
		return "redirect:/competitionPageAll";
	}
	
	//顯示前台圖片
	@GetMapping("/competitionRegistration/showImage/{competitionId}")
	private ResponseEntity<byte[]> showImage(@PathVariable Integer competitionId){
		Competition img = cService.findById(competitionId);
		byte[] imgFile = img.getPhoto();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(imgFile, headers, HttpStatus.OK);
		
	}
	
	
	//////////    後台管理    //////////
	
	// 跳轉單筆頁面
	@GetMapping("/competition/registration/update")
	public String processUpdate(@RequestParam("signupId") Integer id, Model m) {
		CompetitionRegistration registration = crService.findById(id);
		m.addAttribute("registration", registration);

		List<Competition> competitions = cRepo.findAll();
		m.addAttribute("competitions", competitions);
		return "chen/updateCompetitionRegistration";
	}

	// 查詢全部
	@GetMapping("/competition/registration/data")
	public String findALL(Model m) {
		List<CompetitionRegistration> registrations = crService.findAll();
		m.addAttribute("registrations", registrations);
		return "chen/competitionRegistrationData";
	}

	// 透過姓名搜尋
	@PostMapping("/competition/registration/data")
	public String findDataByName(@RequestParam("realName") String realName, Model m) {
		List<CompetitionRegistration> registrations = crService.findByName(realName);
		m.addAttribute("registrations", registrations);
		return "chen/competitionRegistrationData";
	}

	// 修改資料
	@PutMapping("/competition/registration/update")
	public String updatePost(@RequestParam("signupId") Integer signupId,
			@RequestParam("gameNickname") String gameNickname,
			@RequestParam(value = "teamName", required = false) String teamName,
			@RequestParam("realName") String realName, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam(value = "address", required = false) String address) {
		crService.updateRegistrationById(signupId, gameNickname, teamName, realName, email, phone,
				address);
		return "redirect:/competition/registration/data";
	}

	// 刪除資料
	@DeleteMapping("/competition/registration/delete")
	public String deletePost(@RequestParam("signupId") Integer id) {
		crService.deleteById(id);
		return "redirect:/competition/registration/data";
	}
	
	
}
