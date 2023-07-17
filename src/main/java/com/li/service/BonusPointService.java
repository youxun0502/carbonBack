package com.li.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.li.dto.BonusPointDto;
import com.li.dto.BonusShopDto;
import com.li.model.BonusLog;
import com.li.model.BonusPointLog;
import com.li.model.BonusPointLogRepository;
import com.liu.model.Member;
import com.liu.service.MemberService;

@Service
public class BonusPointService {

	@Autowired
	private BonusPointLogRepository bpRepo;
	@Autowired
	private MemberService mService;
	
	public List<BonusPointLog> findAll(){
		return bpRepo.findAll();
	}
	
	public BonusPointLog theLastPoint(Integer memberId) {
		return bpRepo.theLastPoint(memberId);
	}
	
	public List<BonusPointLog> findByMemberId(Integer memberId){
		return bpRepo.findByMemberId(memberId);
	}
	
	@Transactional
	public BonusPointLog insertPointLog(BonusPointLog bpl) {
		return bpRepo.save(bpl);
	}
	
	public BonusPointLog newPointLog(String logtype,Integer memberId,Integer changePoint) {
		BonusPointLog newLog = new BonusPointLog();

			switch (logtype) {
			case "register": {
				newLog.setLogtype(logtype);
				newLog.setMemberId(memberId);
				newLog.setPrepoint(0);
				newLog.setChangepoint(changePoint);
				newLog.setPoint(0+changePoint);
				
				return insertPointLog(newLog);
				
			}
			case "buygame": {
				BonusPointLog theLastPoint = bpRepo.theLastPoint(memberId);
				Integer chp=changePoint/10;
				newLog.setLogtype(logtype);
				newLog.setMemberId(memberId);
				newLog.setPrepoint(theLastPoint.getPoint());
				newLog.setChangepoint(chp);
				newLog.setPoint(theLastPoint.getPoint()+chp);
				
				return insertPointLog(newLog);
				
			}
			case "buybonusitem": {
				BonusPointLog theLastPoint = bpRepo.theLastPoint(memberId);
				newLog.setLogtype(logtype);
				newLog.setMemberId(memberId);
				newLog.setPrepoint(theLastPoint.getPoint());
				newLog.setChangepoint(changePoint);
				newLog.setPoint(theLastPoint.getPoint()+changePoint);
				
				return insertPointLog(newLog);
				
			}
			case "sendmessage": {
				BonusPointLog theLastPoint = bpRepo.theLastPoint(memberId);
				newLog.setLogtype(logtype);
				newLog.setMemberId(memberId);
				newLog.setPrepoint(theLastPoint.getPoint());
				newLog.setChangepoint(changePoint);
				newLog.setPoint(theLastPoint.getPoint()+changePoint);
				
				return insertPointLog(newLog);
				
			}
			default:
				BonusPointLog theLastPoint = bpRepo.theLastPoint(memberId);
				newLog.setLogtype(logtype);
				newLog.setMemberId(memberId);
				newLog.setPrepoint(theLastPoint.getPoint());
				newLog.setChangepoint(changePoint);
				newLog.setPoint(theLastPoint.getPoint()+changePoint);
				
				return insertPointLog(newLog);
			}
	
		
	}
	
	//-----------------------------------------------Convert to Dto-------------------------------------
	public List<BonusPointDto> findAlltoDto(){
		return converttoDtoList(findAll());
	}
	
	public List<BonusPointDto> findByMemberIdtoDto(Integer memberId){
		
		return converttoDtoList(findByMemberId(memberId));
	}
	
	public List<BonusPointDto> converttoDtoList(List<BonusPointLog> bonusLogs){
		List<BonusPointDto> LogDTOList = new ArrayList<>();
		for(BonusPointLog bonusLog : bonusLogs) {
			BonusPointDto itemLogDTO = new BonusPointDto();
			if(bonusLog.getLogId() != null) itemLogDTO.setLogId(bonusLog.getLogId());
			if(bonusLog.getMember() != null) itemLogDTO.setMember(bonusLog.getMember());
			if(bonusLog.getMemberId() != null) itemLogDTO.setMemberId(bonusLog.getMemberId());
			if(bonusLog.getLogtype() != null) itemLogDTO.setLogtype(bonusLog.getLogtype());
			if(bonusLog.getChangepoint() != null) itemLogDTO.setChangepoint(bonusLog.getChangepoint());
			if(bonusLog.getPoint() != null) itemLogDTO.setPoint(bonusLog.getPoint());
			if(bonusLog.getUpdateTime() != null) itemLogDTO.setUpdateTime(bonusLog.getUpdateTime());
			LogDTOList.add(itemLogDTO);
		}
		return LogDTOList;
	}
	
}
