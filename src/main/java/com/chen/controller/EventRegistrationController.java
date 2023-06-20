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

import com.chen.model.Event;
import com.chen.model.EventRegistration;
import com.chen.model.EventRepository;
import com.chen.service.EventRegistrationService;

@Controller
public class EventRegistrationController {

	@Autowired
	private EventRegistrationService erService;
	
	@Autowired
	private EventRepository eRepo;
	
	//跳轉新增頁面
	@GetMapping("/event/registration")
	public String signupPage(Model m) {
		List<Event> events = eRepo.findAll();
		m.addAttribute("events", events);
		return "chen/eventRegistration";
	}
	
	//跳轉單筆頁面
	@GetMapping("/event/registration/update")
	public String processUpdate(@RequestParam("signupId")Integer id, Model m) {
		EventRegistration registration = erService.findById(id);
		m.addAttribute("registration", registration);
		
		List<Event> events = eRepo.findAll();
		m.addAttribute("events", events);
		return "chen/updateEventRegistration";
	}
	
	//查詢全部
	@GetMapping("/event/registration/data")
	public String findALL(Model m) {
		List<EventRegistration> registrations = erService.findAll();
		m.addAttribute("registrations", registrations);
		return "chen/eventRegistrationData";
	}
	
	//透過姓名搜尋
	@PostMapping("/event/registration/data")
	public String findDataByName(@RequestParam("realName")String realName,Model m) {
		List<EventRegistration> registrations = erService.findByName(realName);
		m.addAttribute("registrations", registrations);
		return "chen/eventRegistrationData";
	}
	
	//新增資料
	@PostMapping("/event/registration/insert")
	public String inserData(@RequestParam("eventId")Integer eventId,@RequestParam("realName")String realName,@RequestParam("email")String email,@RequestParam("phone")String phone,
							@RequestParam("address")String address) {
		EventRegistration er = new EventRegistration();
		er.setEventId(eventId);
		er.setRealName(realName);
		er.setEmail(email);
		er.setPhone(phone);
		er.setAddress(address);
		erService.insert(er);
		return "redirect:/event/registration";
	}
	
	//修改資料
	@PutMapping("/event/registration/update")
	public String updatePost(@RequestParam("signupId")Integer signupId,@RequestParam("eventId")Integer eventId,@RequestParam("realName")String realName,
							@RequestParam("email")String email,@RequestParam("phone")String phone,@RequestParam("address")String address) {
		erService.updateRegistrationById(signupId, eventId, realName, email, phone, address);
		return "redirect:/event/registration/data";
	}
	
	//刪除資料
	@DeleteMapping("/event/registration/delete")
	public String deletePost(@RequestParam("signupId")Integer id) {
		erService.deleteById(id);
		return "redirect:/event/registration/data";
	}
}
