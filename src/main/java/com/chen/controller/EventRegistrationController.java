package com.chen.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.chen.model.Event;
import com.chen.model.EventRegistration;
import com.chen.model.EventRepository;
import com.chen.service.EventRegistrationService;
import com.chen.service.EventService;

@Controller
public class EventRegistrationController {

	@Autowired
	private EventRegistrationService erService;

	@Autowired
	private EventRepository eRepo;
	
	@Autowired
	private EventService eService;

	//////////    前台管理    //////////
	
	
	// 跳轉活動總覽頁面
	@GetMapping("/eventPageAll")
	public String eventFrontPageAll(Model m) {
		List<Event> events = eRepo.findAll();
		m.addAttribute("events", events);
		return "chen/eventFrontPageAll";
	}
	
	// 跳轉活動分類頁面
	@GetMapping("/eventPageOne")
	public String eventFrontPageOne(@RequestParam("gameId")Integer gameId,Model m) {
		List<Event> events = erService.findByGameId(gameId);
		m.addAttribute("events", events);
		return "chen/eventFrontPageOne";
	}
	
	// 跳轉活動細節頁面
	@GetMapping("/eventPageDetail")
	public String eventFrontPageDetail(@RequestParam("eventId")Integer eventId,Model m) {
		System.out.println("eventId =" + eventId);
		Event event = eService.findById(eventId);
		m.addAttribute("event", event);
		return "chen/eventFrontPageDetail";
	}
	
	// 跳轉新增頁面
	@GetMapping("/eventRegistration")
	public String signupPage(Model m) {
		List<Event> events = eRepo.findAll();
		m.addAttribute("events", events);
		return "chen/eventRegistration";
	}
	
	// 新增資料
	@PostMapping("/eventRegistration/insert")
	public String inserData(@RequestParam("eventId") Integer eventId, @RequestParam("realName") String realName,
			@RequestParam("email") String email, @RequestParam("phone") String phone,
			@RequestParam(value = "address", required = false) String address) {
		EventRegistration er = new EventRegistration();
		er.setEventId(eventId);
		er.setRealName(realName);
		er.setEmail(email);
		er.setPhone(phone);
		er.setAddress(address);
		erService.insert(er);
		return "redirect:/eventRegistration";
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
	public String updatePost(@RequestParam("signupId") Integer signupId, @RequestParam("eventId") Integer eventId,
			@RequestParam("realName") String realName, @RequestParam("email") String email,
			@RequestParam("phone") String phone, @RequestParam("address") String address) {
		erService.updateRegistrationById(signupId, eventId, realName, email, phone, address);
		return "redirect:/event/registration/data";
	}

	// 刪除資料
	@DeleteMapping("/event/registration/delete")
	public String deletePost(@RequestParam("signupId") Integer id) {
		erService.deleteById(id);
		return "redirect:/event/registration/data";
	}
	
}
