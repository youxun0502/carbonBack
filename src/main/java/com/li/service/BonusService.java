package com.li.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public BonusItem updateBonusItemById(Integer id, String newName,Integer newPrice,String newDes,Boolean status) {
		Optional<BonusItem> optional = bRepo.findById(id);
		
		if(optional.isPresent()) {
			BonusItem msg = optional.get();
			msg.setBonusName(newName);
			msg.setBonusPrice(newPrice);
			msg.setBonusDes(newDes);
			msg.setStatus(status);
			return msg;
		}
		
		System.out.println("no update data");
		
		return null;
	}
	
	public List<BonusItem> findByName(String str){
		
		String findString="%"+str+"%";
		return bRepo.findByName(findString);
	}

}
