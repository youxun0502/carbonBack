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


import com.chen.model.Event;
import com.chen.model.EventRegistration;
import com.chen.model.EventRegistrationRepository;
import com.chen.model.EventRepository;
import com.chen.service.EventRegistrationService;
import com.chen.service.EventService;
import com.evan.dao.GameRepository;
import com.evan.model.Game;
import com.liu.model.Member;
import com.liu.service.MemberService;


@Controller
public class EventRegistrationController {

	@Autowired
	private EventRegistrationService erService;

	@Autowired
	private EventRepository eRepo;
	
	@Autowired
	private EventService eService;
	
	@Autowired
	private GameRepository gRepo;
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private EventRegistrationRepository erRepo;
	

	//////////    前台管理    //////////
	// 跳轉報名紀錄頁面
	@GetMapping("/registrationRecord")
	public String registrationRecordPage(@RequestParam(value = "memberId", required = false)Integer memberId, Model m) {
		
		List<EventRegistration> recordList = erRepo.findMemberRecord(memberId);
		m.addAttribute("recordList", recordList);
		
		return "chen/registrationRecord";
	}
	
	// 報名紀錄分類查詢
	@ResponseBody
	@GetMapping("/registrationRecord/category")
	public List<Event> findRegistrationRecordByGameName(@RequestParam("eventName") String eventName, @RequestParam("memberId") Integer memberId) {
	    List<Event> recordList = eRepo.findMemberRecord(memberId, eventName);
	    return recordList;
	}
	
	// 跳轉活動總覽頁面
	@GetMapping("/eventPageAll")
	public String eventFrontPageAll(@RequestParam(name="p", defaultValue = "1") Integer pageNumber, Model m) {
		List<Game> games = gRepo.findAll();
		m.addAttribute("games", games);
		
		Page<Event> page = erService.findByPageAll(pageNumber);
		m.addAttribute("page", page);
		return "chen/eventFrontPageAll";
	}
	
	/*
	// 跳轉活動分類頁面
	@GetMapping("/eventPageOne")
	public String eventFrontPageOne(@RequestParam("gameId")Integer gameId,Model m) {
		List<Game> games = gRepo.findAll();
		m.addAttribute("games", games);
		
		List<Event> events = erService.findByGameId(gameId);
		m.addAttribute("events", events);
		return "chen/eventFrontPageOne";
	}
	*/
	// 跳轉活動分類頁面
	@GetMapping("/eventPageOne")
	public String eventFrontPageOne(@RequestParam(name="p", defaultValue = "1") Integer pageNumber,@RequestParam(value = "gameId", required = false)Integer gameId,Model m) {
		List<Game> games = gRepo.findAll();
		m.addAttribute("games", games);
		
		Page<Event> page = erService.findByPageOne(pageNumber,gameId);
		m.addAttribute("page", page);
		return "chen/eventFrontPageOne";
	}
	
	// 跳轉活動細節頁面
	@GetMapping("/eventPageDetail")
	public String eventFrontPageDetail(@RequestParam("eventId")Integer eventId,Model m) {
		Event event = eService.findById(eventId);
		m.addAttribute("event", event);
		return "chen/eventFrontPageDetail";
	}
	
	// 跳轉新增頁面
	@GetMapping("/eventRegistration")
	public String signupPage(@RequestParam("eventId")Integer eventId,@RequestParam(value = "memberId", required = false)Integer memberId, Model m) {
		
		if(memberId != null) {
			Member member = mService.findById(memberId);
			m.addAttribute("member", member);
		}
		
		Event event = eService.findById(eventId);
		m.addAttribute("event", event);
		return "chen/eventRegistration";
	}
	
	// 新增資料
	@PostMapping("/eventRegistration/insert")
	public String inserData(@RequestParam("eventId") Integer eventId, @RequestParam("realName") String realName,@RequestParam(value = "memberId", required = false) Integer memberId,
	        @RequestParam("email") String email, @RequestParam("phone") String phone,
	        @RequestParam(value = "address", required = false) String address) {
	    EventRegistration er = new EventRegistration();
	    er.setEventId(eventId);
	    er.setRealName(realName);
	    er.setEmail(email);
	    er.setPhone(phone);
	    er.setAddress(address);
	    er.setMemberId(memberId);
	    erService.insert(er);
	    return "redirect:/eventPageAll";
	}
	
	//顯示前台圖片
	@GetMapping("/eventRegistration/showImage/{eventId}")
	private ResponseEntity<byte[]> showImage(@PathVariable Integer eventId){
		Event img = eService.findById(eventId);
		byte[] imgFile = img.getPhoto();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(imgFile, headers, HttpStatus.OK);
		
	}
	
	//前台換頁Ajax
	@ResponseBody
	@GetMapping("/messages/api/page")
	public Page<Event> showMessagesApi(@RequestParam(name="p",defaultValue = "1") Integer pageNumber){
		Page<Event> page = erService.findByPageAll(pageNumber);
		return page;
	}
	
	// 刪除資料
	@DeleteMapping("/registrationRecord/delete")
	public String deleteRegistrationRecord(@RequestParam("signupId") Integer id,@RequestParam(value = "memberId", required = false)Integer memberId, Model m) {
		erService.deleteById(id);
		List<EventRegistration> recordList = erRepo.findMemberRecord(memberId);
		m.addAttribute("recordList", recordList);
		
		return "chen/registrationRecord";
	}
	
	


	
	
	//////////    後台管理    //////////
	
	// 跳轉單筆頁面
	@GetMapping("/event/registration/update")
	public String processUpdate(@RequestParam("signupId") Integer id, Model m) {
		EventRegistration registration = erService.findById(id);
		m.addAttribute("registration", registration);

		List<Event> events = eRepo.findAll();
		m.addAttribute("events", events);
		return "chen/updateEventRegistration";
	}

	// 查詢全部
	@GetMapping("/event/registration/data")
	public String findALL(Model m) {
		List<Event> events = eService.findAll();
		m.addAttribute("events", events);
		
		List<EventRegistration> registrations = erService.findAll();
		m.addAttribute("registrations", registrations);
		return "chen/eventRegistrationData";
	}

	// 透過姓名搜尋
	@PostMapping("/event/registration/data")
	public String findDataByName(@RequestParam("realName") String realName, Model m) {
		List<EventRegistration> registrations = erService.findByName(realName);
		m.addAttribute("registrations", registrations);
		return "chen/eventRegistrationData";
	}

	// 修改資料
	@PutMapping("/event/registration/update")
	public String updatePost(@RequestParam("signupId") Integer signupId, 
			@RequestParam("realName") String realName, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam(value = "address", required = false) String address) {
		erService.updateRegistrationById(signupId, realName, email, phone, address);
		return "redirect:/event/registration/data";
	}

	// 刪除資料
	@DeleteMapping("/event/registration/delete")
	public String deletePost(@RequestParam("signupId") Integer id) {
		erService.deleteById(id);
		return "redirect:/event/registration/data";
	}
	
	// 分類顯示
	@ResponseBody
	@GetMapping("/event/registration/category")
	public List<EventRegistration> showByCategory(@RequestParam("eventId") Integer eventId) {
	    List<EventRegistration> registrations = erRepo.findByEventId(eventId);
	    return registrations;
	}
	
}
