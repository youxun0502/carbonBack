package com.ni.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ni.dto.ItemLogDTO;
import com.ni.model.ItemLog;
import com.ni.model.ItemLogRepository;

@Service
public class ItemLogService {

	@Autowired
	private ItemLogRepository itemLogRepo;
	
	public List<ItemLogDTO> findAll() {
		return convertToDTOList(itemLogRepo.findAll());
	}
	
	public ItemLogDTO findById(Integer id) {
		Optional<ItemLog> optional = itemLogRepo.findById(id);
		if(optional.isPresent()) {
			return convertToDTO(optional.get());
		}
		return null;
	}
	
	public ItemLog insert(ItemLogDTO itemLog) {
		ItemLog log = findTotalById(itemLog.getMemberId(), itemLog.getItemId());
		if(log != null) {
			itemLog.setTotal(log.getTotal() + itemLog.getQuantity()); 
		} else {
			itemLog.setTotal(itemLog.getQuantity());
		}
		return itemLogRepo.save(convertToitemLog(itemLog));
	}
	

	public ItemLog findTotalById(Integer memberId, Integer itemId) {
		return itemLogRepo.findByMemberIdAndItemId(memberId, itemId);
	}
	
	public List<ItemLogDTO> findByMemberId(Integer memberId) {
		return convertToDTOList(itemLogRepo.findByMemberId(memberId));
	}
	
	public Page<ItemLog> findOrderHistory(Integer memberId, Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 10);
		return itemLogRepo.findOrderHistory(memberId, pgb);
	}
	
//	======================= 轉換 DTO 和 Entity =======================
	public List<ItemLogDTO> convertToDTOList(List<ItemLog> itemLogs){
		List<ItemLogDTO> itemLogDTOList = new ArrayList<>();
		for(ItemLog itemLog : itemLogs) {
			ItemLogDTO itemLogDTO = new ItemLogDTO();
			if(itemLog.getId() != null) itemLogDTO.setId(itemLog.getId());
			if(itemLog.getOrdId() != null) itemLogDTO.setOrdId(itemLog.getOrdId());
			if(itemLog.getItemId() != null) itemLogDTO.setItemId(itemLog.getItemId());
			if(itemLog.getMemberId() != null) itemLogDTO.setMemberId(itemLog.getMemberId());
			if(itemLog.getQuantity() != null) itemLogDTO.setQuantity(itemLog.getQuantity());
			if(itemLog.getTotal() != null) itemLogDTO.setTotal(itemLog.getTotal());
			if(itemLog.getCreateTime() != null) itemLogDTO.setCreateTime(itemLog.getCreateTime());
			if(itemLog.getMember() != null) itemLogDTO.setMember(itemLog.getMember());
			if(itemLog.getGameItem() != null) itemLogDTO.setGameItem(itemLog.getGameItem());
			if(itemLog.getItemOrder() != null) itemLogDTO.setItemOrder(itemLog.getItemOrder());
			itemLogDTOList.add(itemLogDTO);
		}
		return itemLogDTOList;
	}
	
	public ItemLogDTO convertToDTO(ItemLog itemLog) {
		ItemLogDTO itemLogDTO = new ItemLogDTO();
		if(itemLog.getId() != null) itemLogDTO.setId(itemLog.getId());
		if(itemLog.getOrdId() != null) itemLogDTO.setOrdId(itemLog.getOrdId());
		if(itemLog.getItemId() != null) itemLogDTO.setItemId(itemLog.getItemId());
		if(itemLog.getMemberId() != null) itemLogDTO.setMemberId(itemLog.getMemberId());
		if(itemLog.getQuantity() != null) itemLogDTO.setQuantity(itemLog.getQuantity());
		if(itemLog.getTotal() != null) itemLogDTO.setTotal(itemLog.getTotal());
		if(itemLog.getCreateTime() != null) itemLogDTO.setCreateTime(itemLog.getCreateTime());
		if(itemLog.getMember() != null) itemLogDTO.setMember(itemLog.getMember());
		if(itemLog.getGameItem() != null) itemLogDTO.setGameItem(itemLog.getGameItem());
		if(itemLog.getItemOrder() != null) itemLogDTO.setItemOrder(itemLog.getItemOrder());
		return itemLogDTO;
	}
	
	public ItemLog convertToitemLog(ItemLogDTO itemLogDTO) {
		ItemLog itemLog = new ItemLog();
		if(itemLogDTO.getId() != null) itemLog.setId(itemLogDTO.getId());
		if(itemLogDTO.getOrdId() != null) itemLog.setOrdId(itemLogDTO.getOrdId());
		if(itemLogDTO.getItemId() != null) itemLog.setItemId(itemLogDTO.getItemId());
		if(itemLogDTO.getMemberId() != null) itemLog.setMemberId(itemLogDTO.getMemberId());
		if(itemLogDTO.getQuantity() != null) itemLog.setQuantity(itemLogDTO.getQuantity());
		if(itemLogDTO.getTotal() != null) itemLog.setTotal(itemLogDTO.getTotal());
		if(itemLogDTO.getCreateTime() != null) itemLog.setCreateTime(itemLogDTO.getCreateTime());
		if(itemLogDTO.getMember() != null) itemLog.setMember(itemLogDTO.getMember());
		if(itemLogDTO.getGameItem() != null) itemLog.setGameItem(itemLogDTO.getGameItem());
		if(itemLogDTO.getItemOrder() != null) itemLog.setItemOrder(itemLogDTO.getItemOrder());
		return itemLog;
	}
}
