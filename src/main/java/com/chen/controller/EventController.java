package com.chen.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.chen.model.Event;
import com.chen.service.EventService;
import com.evan.dao.GameRepository;
import com.evan.model.Game;

@Controller
public class EventController {

	@Autowired
	private EventService eService;
	
	@Autowired
	private GameRepository gRepo;
	
	//跳轉新增頁面
	@GetMapping("/event/add")
	public String insertData(Model m) {
		List<Game> gameList = gRepo.findAll();
		m.addAttribute("gameList", gameList);
		return "chen/insertEvent";
	}
	
	//跳轉單筆頁面
	@GetMapping("/event/update")
	public String processUpdate(@RequestParam("eventId")Integer id,Model m) {
		Event event = eService.findById(id);
		m.addAttribute("event", event);
		return "chen/updateEvent";
	}
		
	//查詢全部
	@GetMapping("/event/data")
	public String findALL(Model m) {
		List<Event> events = eService.findAll();
		m.addAttribute("events", events);
		return "chen/eventData";
	}
	
	//透過活動名稱搜尋
	@PostMapping("/event/data")
	public String findDataByName(@RequestParam("eventName")String eventName,Model m) {
		List<Event> events = eService.findByName(eventName);
		m.addAttribute("events", events);
		return "chen/eventData";
	}
	
	//新增資料
	@PostMapping("/event/insert")
	public String insertData(@RequestParam(value = "photo", required = false)MultipartFile photo,@RequestParam("gameId")Integer gameId,@RequestParam("name")String name,@RequestParam(value = "desc", required = false)String desc,
							@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,
							@RequestParam(value = "timeLimitedDiscount", required = false)String timeLimitedDiscount,@RequestParam("location")String location,
							@RequestParam(value = "quotaLimited", required = false)Integer quotaLimited,@RequestParam("deadline")String deadline,@RequestParam(value = "fee", required = false)Integer fee) {
			try {
				if(photo != null && !photo.isEmpty()) {
					Event e = new Event();
					e.setPhoto(photo.getBytes());
					e.setGameId(gameId);
					e.setName(name);
					e.setDescription(desc);
					e.setStartDate(startDate);
					e.setEndDate(endDate);
					e.setTimeLimitedDiscount(timeLimitedDiscount);
					e.setLocation(location);
					e.setQuotaLimited(quotaLimited);
					e.setDeadline(deadline);
					e.setFee(fee);
					eService.insert(e);
				}
				return "redirect:/event/data";
			} catch (IOException e1) {
				e1.printStackTrace();
				return "chen/insertEvent";
			}
		
	}
	
	//修改資料
	@PutMapping("/event/update")
	public String updatePost(@RequestParam("eventId")Integer eventId,@RequestParam("name")String name,@RequestParam(value = "desc", required = false)String description,
							@RequestParam("startDate")String startDate,@RequestParam("endDate")String endDate,@RequestParam(value = "timeLimitedDiscount", required = false)String timeLimitedDiscount,
							@RequestParam("location")String location,@RequestParam(value = "quotaLimited", required = false)Integer quotaLimited,@RequestParam("deadline")String deadline,
							@RequestParam(value = "fee", required = false)Integer fee) {
		eService.updateEventById(eventId, name, description, startDate, endDate, timeLimitedDiscount, location, quotaLimited, deadline, fee);
		return "redirect:/event/data";
	}
	
	//刪除資料
	@DeleteMapping("/event/delete")
	public String deletePost(@RequestParam("eventId")Integer id) {
		eService.deleteById(id);
		return "redirect:/event/data";
	}
	
	//顯示後台圖片
	@GetMapping("/event/showImage/{eventId}")
	private ResponseEntity<byte[]> showImage(@PathVariable Integer eventId){
		Event img = eService.findById(eventId);
		byte[] imgFile = img.getPhoto();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(imgFile, headers, HttpStatus.OK);
		
	}
	
}
