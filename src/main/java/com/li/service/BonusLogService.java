package com.li.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.li.model.BonusLog;
import com.li.model.BonusLogRepository;

@Service
public class BonusLogService {

	@Autowired
	private BonusLogRepository blRepo;
	
	public List<BonusLog> findAll(){
		return blRepo.findAll();
	}
	
	public List<BonusLog> findByMemberId(Integer id){
		return blRepo.findByMemberId(id);
	}
}
