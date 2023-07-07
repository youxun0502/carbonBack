package com.chen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chen.model.Competition;
import com.chen.model.CompetitionRepository;

import jakarta.transaction.Transactional;

@Service
public class CompetitionService {

	@Autowired
	private CompetitionRepository cRepo;
	
	
	//查詢全部
	public List<Competition> findAll(){
		return cRepo.findAll();
	}
	
	//查詢單筆
	public Competition findById(Integer id) {
		Optional<Competition> optional = cRepo.findById(id);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	//透過遊戲搜尋
	public List<Competition> findByName(String name){
		return cRepo.findByNamelike(name);
	}
	
	//新增資料
	public void insert(Competition comp) {
		cRepo.save(comp);
	}
	
	//修改資料
	@Transactional
	public Competition updateCompetitionById(Integer id, String name, String mode,String startDate,String endDate, String location,
											Integer prize, Integer quotaLimited , String deadline,Integer fee,String desc){
			Optional<Competition> optional = cRepo.findById(id);
			if(optional.isPresent()) {
				Competition comp = optional.get();
				comp.setName(name);
				comp.setMode(mode);
				comp.setStartDate(startDate);
				comp.setEndDate(endDate);
				comp.setLocation(location);
				comp.setPrize(prize);
				comp.setQuotaLimited(quotaLimited);
				comp.setDeadline(deadline);
				comp.setFee(fee);
				comp.setDescription(desc);
				return comp;
			}
			return null;
	}
	
	//刪除資料
	public void deleteById(Integer id) {
		cRepo.deleteById(id);
	}
	
	
}
