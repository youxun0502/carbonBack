package com.li.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.li.model.BonusItem;
import com.li.model.BonusItemRepository;

@Service
public class BonusService {

	@Autowired
	BonusItemRepository bRepo;

	public BonusItem insertBonusItem(BonusItem bi) {
		return bRepo.save(bi);
	}

	public List<BonusItem> findAll() {
		return bRepo.findAll();
	}
	
	public void deleteById(Integer id) {
		bRepo.deleteById(id);
	}

	public BonusItem getBonusItemById(Integer id) {
		Optional<BonusItem> optional = bRepo.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}

		return null;
	}
	
	@Transactional
	public BonusItem updateBonusItemById(Integer id, String newName,Integer newPrice,String newDes,Boolean status,String newType) {
		Optional<BonusItem> optional = bRepo.findById(id);
		
		if(optional.isPresent()) {
			BonusItem msg = optional.get();
			msg.setBonusName(newName);
			msg.setBonusPrice(newPrice);
			msg.setBonusDes(newDes);
			msg.setStatus(status);
			msg.setBonusType(newType);
			return msg;
		}
		
		System.out.println("no update data");
		
		return null;
	}
	
	public List<BonusItem> findByName(String str){
		
		String findString="%"+str+"%";
		return bRepo.findByName(findString);
	}
	public List<BonusItem> findAllAvatar(){
		return bRepo.findByBonusType("avatar");
	}
	public List<BonusItem> findAllFrame(){
		return bRepo.findByBonusType("frame");
	}
	public List<BonusItem> findAllBackground(){
		return bRepo.findByBonusType("background");
	}
	public Page<BonusItem> findByBonusTypePage(String bonusType,Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber - 1, 6);
		return bRepo.findByBonusTypePage(bonusType, pgb);
	}

}
