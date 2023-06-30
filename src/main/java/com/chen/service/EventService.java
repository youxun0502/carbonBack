package com.chen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.model.Event;
import com.chen.model.EventRepository;

import jakarta.transaction.Transactional;

@Service
public class EventService {

	@Autowired
	private EventRepository eRepo;
	
	//查詢全部
	public List<Event> findAll(){
		return eRepo.findAll();
	}
	
	//查詢單筆
	public Event findById(Integer id) {
		Optional<Event> optional = eRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//透過遊戲搜尋
	public List<Event> findByName(String name){
		return eRepo.findByNamelike(name);
	}
		
	//新增資料
	public void insert(Event e) {
		eRepo.save(e);
	}
	
	//修改資料
	@Transactional
	public Event updateEventById(Integer eventId, String name, String description, String startDate, String endDate,
								String timeLimitedDiscount, String location, Integer quotaLimited, String deadline, Integer fee) {
		Optional<Event> optional = eRepo.findById(eventId);
		if(optional.isPresent()) {
			Event e = optional.get();
			e.setName(name);
			e.setDescription(description);
			e.setStartDate(startDate);
			e.setEndDate(endDate);
			e.setTimeLimitedDiscount(timeLimitedDiscount);
			e.setLocation(location);
			e.setQuotaLimited(quotaLimited);
			e.setDeadline(deadline);
			e.setFee(fee);
			return e;
		}
		return null;
	}
	
	//刪除資料
	public void deleteById(Integer id) {
		eRepo.deleteById(id);
	}
}
