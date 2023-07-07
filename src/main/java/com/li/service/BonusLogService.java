package com.li.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.li.dto.BonusShopDto;
import com.li.model.BonusLog;
import com.li.model.BonusLogRepository;
import com.ni.dto.ItemLogDTO;
import com.ni.model.ItemLog;

import jakarta.transaction.Transactional;

@Service
public class BonusLogService {

	@Autowired
	private BonusLogRepository blRepo;
	
	@Transactional
	public BonusLog newBonusLog(BonusLog bl) {
		return blRepo.save(bl);
	}
	
	public List<BonusLog> findAll(){
		return blRepo.findAll();
	}
	
	public List<BonusLog> findByMemberId(Integer id){
		return blRepo.findByMemberId(id);
	}
	
	public List<BonusShopDto> findByMemberIdtoDto(Integer id){
		return converttoDtoList(findByMemberId(id));
	}
	
	public List<BonusShopDto> converttoDtoList(List<BonusLog> bonusLogs){
		List<BonusShopDto> LogDTOList = new ArrayList<>();
		for(BonusLog bonusLog : bonusLogs) {
			BonusShopDto itemLogDTO = new BonusShopDto();
			if(bonusLog.getLogId() != null) itemLogDTO.setLogId(bonusLog.getLogId());
			if(bonusLog.getMember() != null) itemLogDTO.setMember(bonusLog.getMember());
			if(bonusLog.getMemberId() != null) itemLogDTO.setMemberId(bonusLog.getMemberId());
			if(bonusLog.getBonusitem() != null) itemLogDTO.setBonusitem(bonusLog.getBonusitem());
			if(bonusLog.getBuyDate() != null) itemLogDTO.setBuyDate(bonusLog.getBuyDate());
			LogDTOList.add(itemLogDTO);
		}
		return LogDTOList;
	}
}
