package com.ni.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ni.model.ItemLog;
import com.ni.model.ItemLogRepository;

@Service
public class ItemLogService {

	@Autowired
	private ItemLogRepository itemLogRepo;
	
	public List<ItemLog> findAll() {
		return itemLogRepo.findAll();
	}
	
	public ItemLog findById(Integer id) {
		Optional<ItemLog> optional = itemLogRepo.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<ItemLog> findByMemberId() {
		return null;
	}
	
	public ItemLog insert(ItemLog itemLog) {
		return itemLogRepo.save(itemLog);
	}
	
	@Transactional
	public ItemLog updateById(ItemLog itemLog) {
		Optional<ItemLog> optional = itemLogRepo.findById(itemLog.getId());
		if(optional.isPresent()) {
			ItemLog log = optional.get();
			if(log.getOrdId() != null) log.setOrdId(itemLog.getOrdId());
			if(log.getItemId() != null) log.setItemId(itemLog.getItemId());
			if(log.getMember() != null) log.setMemberId(itemLog.getMemberId());
			if(log.getItemAmount() != null) log.setItemAmount(itemLog.getItemAmount());
			return log;
		}
		System.out.println("no update data");
		return null;
	}
	
	public void delete(Integer id) {
		itemLogRepo.deleteById(id);
	}
}
