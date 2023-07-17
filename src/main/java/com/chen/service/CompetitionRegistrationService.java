package com.chen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.chen.model.Competition;
import com.chen.model.CompetitionRegistration;
import com.chen.model.CompetitionRegistrationRepository;
import com.chen.model.CompetitionRepository;

import jakarta.transaction.Transactional;

@Service
public class CompetitionRegistrationService {

	@Autowired
	private CompetitionRegistrationRepository crRepo;
	
	@Autowired
	private CompetitionRepository cRepo;
	
	//新增資料
	public void insert(CompetitionRegistration cr) {
		crRepo.save(cr);
	}
	
	//查詢全部
	public List<CompetitionRegistration> findAll(){
		return crRepo.findAll();
	}
	
	//查詢單筆
	public CompetitionRegistration findById(Integer id) {
		Optional<CompetitionRegistration> optional = crRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//透過遊戲搜尋
	public List<CompetitionRegistration> findByName(String name){
		return crRepo.findByNamelike(name);
	}
	
	//修改資料
	@Transactional
	public CompetitionRegistration updateRegistrationById(Integer signupId, String gameNickname, String teamName,
														String realName, String email, String phone, String address) {
		Optional<CompetitionRegistration> optional = crRepo.findById(signupId);
		if(optional.isPresent()) {
			CompetitionRegistration cr = optional.get();
			cr.setGameNickname(gameNickname);
			cr.setTeamName(teamName);
			cr.setRealName(realName);
			cr.setEmail(email);
			cr.setPhone(phone);
			cr.setAddress(address);
			return cr;
		}
		return null;
	}
	//刪除資料
	public void deleteById(Integer id) {
		crRepo.deleteById(id);
	}
	
	//總覽分頁
	public Page<Competition> findByPageAll(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1, 4, Sort.Direction.ASC, "startDate");
		
		Page<Competition> page = cRepo.findAll(pgb);
		
		return page;
	}
	
	//分類分頁
	public Page<Competition> findByPageOne(Integer pageNumber,Integer gameId){
		Pageable pgb = PageRequest.of(pageNumber-1, 4, Sort.Direction.ASC, "startDate");
		
		Page<Competition> page = cRepo.findByGameId(gameId,pgb);
		
		return page;
	}
}
