package com.li.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.li.model.BonusLog;
import com.li.model.BonusPointLog;
import com.li.model.BonusPointLogRepository;

@Service
public class BonusPointService {

	@Autowired
	private BonusPointLogRepository bpRepo;
	
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
	
}
