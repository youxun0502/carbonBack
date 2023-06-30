package com.chen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.model.EventRegistration;
import com.chen.model.EventRegistrationRepository;

import jakarta.transaction.Transactional;

@Service
public class EventRegistrationService {

	@Autowired
	private EventRegistrationRepository erRepo;
	
	//新增資料
	public void insert(EventRegistration er) {
		erRepo.save(er);
	}
	
	//查詢全部
	public List<EventRegistration> findAll(){
		return erRepo.findAll();
	}
	
	//查詢單筆
	public EventRegistration findById(Integer id) {
		Optional<EventRegistration> optional = erRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//透過遊戲搜尋
	public List<EventRegistration> findByName(String name){
		return erRepo.findByNamelike(name);
	}
	
	//修改資料
	@Transactional
	public EventRegistration updateRegistrationById(Integer signupId, Integer eventId, String realName, String email, String phone, 
													String address) {
		Optional<EventRegistration> optional = erRepo.findById(signupId);
		if(optional.isPresent()) {
			EventRegistration er = optional.get();
			er.setEventId(eventId);
			er.setRealName(realName);
			er.setEmail(email);
			er.setPhone(phone);
			er.setAddress(address);
			return er;
		}
		return null;
	}
	
	//刪除資料
	public void deleteById(Integer id) {
		erRepo.deleteById(id);
	}
}
