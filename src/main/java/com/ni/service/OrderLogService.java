package com.ni.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ni.dto.OrderLogDTO;
import com.ni.model.OrderLog;
import com.ni.model.OrderLogRepository;

@Service
public class OrderLogService {

	@Autowired
	private OrderLogRepository orderRepo;
	
	public List<OrderLogDTO> findAll() {
		return convertToDTOList(orderRepo.findAll());
	}
	
	public OrderLogDTO findById(Integer logId) {
		Optional<OrderLog> optional = orderRepo.findById(logId);
		if(optional.isPresent()) {
			return convertToDTO(optional.get());
		}
		return null;
	}
	
	public OrderLog insert(OrderLogDTO orderLog) {
		return orderRepo.save(convertToOrderLog(orderLog));
	}
	
	@Transactional
	public OrderLog updateStatusById(Integer logId, Integer status) {
		Optional<OrderLog> optional = orderRepo.findById(logId);
		if(optional.isPresent()) {
			OrderLog order = optional.get();
			order.setStatus(status);;
			return order;
		}
		System.out.println("no update data");
		return null;
	}
	
	public List<OrderLogDTO> findSellItemList(Integer gameId, String itemName) {
		return convertToDTOList(orderRepo.findSellItemList(gameId, itemName));
	}
	
	public List<Map<String, Object>> findOrderList() {
		List<Object[]> results = orderRepo.countOrderList();
	    List<Map<String, Object>> orderList = new ArrayList<>();
	    for (Object[] result : results) {
	    	Map<String, Object> order = new HashMap<>();
	        order.put("itemId", result[0]);
	        order.put("itemName", result[1]);
	        order.put("itemImgName", result[2]);
	        order.put("gameId", result[3]);
	        order.put("price", result[4]);
	        orderList.add(order);
	    }
		return orderList;
	}
	
	public List<OrderLogDTO> findByItemIdAndStatus(Integer itemId) {
		return convertToDTOList(orderRepo.findByItemIdAndStatus(itemId));
	}
	
	
//	======================= 轉換 DTO 和 Entity =======================
	public List<OrderLogDTO> convertToDTOList(List<OrderLog> orderLogs) {
		List<OrderLogDTO> orderDTOList = new ArrayList<>();
		for(OrderLog orderLog : orderLogs) {
			OrderLogDTO orderDTO = new OrderLogDTO();
			if(orderLog.getLogId() != null) orderDTO.setLogId(orderLog.getLogId());
			if(orderLog.getItemId() != null) orderDTO.setItemId(orderLog.getItemId());
			if(orderLog.getBuyer() != null) orderDTO.setBuyer(orderLog.getBuyer());
			if(orderLog.getSeller() != null) orderDTO.setSeller(orderLog.getSeller());
			if(orderLog.getQuantity() != null) orderDTO.setQuantity(orderLog.getQuantity());
			if(orderLog.getPrice() != null) orderDTO.setPrice(orderLog.getPrice());
			if(orderLog.getStatus() != null) orderDTO.setStatus(orderLog.getStatus());
			if(orderLog.getCreateTime() != null) orderDTO.setCreateTime(orderLog.getCreateTime());
			if(orderLog.getUpdateTime() != null) orderDTO.setUpdateTime(orderLog.getUpdateTime());
			if(orderLog.getBuy() != null) orderDTO.setBuy(orderLog.getBuy());
			if(orderLog.getSell() != null) orderDTO.setSell(orderLog.getSell());
			if(orderLog.getGameItem() != null) orderDTO.setGameItem(orderLog.getGameItem());
			orderDTOList.add(orderDTO);
		}
		return orderDTOList;
	}
	
	public OrderLogDTO convertToDTO(OrderLog orderLog) {
		OrderLogDTO orderDTO = new OrderLogDTO();
		if(orderLog.getLogId() != null) orderDTO.setLogId(orderLog.getLogId());
		if(orderLog.getItemId() != null) orderDTO.setItemId(orderLog.getItemId());
		if(orderLog.getBuyer() != null) orderDTO.setBuyer(orderLog.getBuyer());
		if(orderLog.getSeller() != null) orderDTO.setSeller(orderLog.getSeller());
		if(orderLog.getQuantity() != null) orderDTO.setQuantity(orderLog.getQuantity());
		if(orderLog.getPrice() != null) orderDTO.setPrice(orderLog.getPrice());
		if(orderLog.getStatus() != null) orderDTO.setStatus(orderLog.getStatus());
		if(orderLog.getCreateTime() != null) orderDTO.setCreateTime(orderLog.getCreateTime());
		if(orderLog.getUpdateTime() != null) orderDTO.setUpdateTime(orderLog.getUpdateTime());
		if(orderLog.getBuy() != null) orderDTO.setBuy(orderLog.getBuy());
		if(orderLog.getSell() != null) orderDTO.setSell(orderLog.getSell());
		if(orderLog.getGameItem() != null) orderDTO.setGameItem(orderLog.getGameItem());
		return orderDTO;
	}
	
	public OrderLog convertToOrderLog(OrderLogDTO orderLogDTO) {
		OrderLog order = new OrderLog();
		if(orderLogDTO.getLogId() != null) order.setLogId(orderLogDTO.getLogId());
		if(orderLogDTO.getItemId() != null) order.setItemId(orderLogDTO.getItemId());
		if(orderLogDTO.getBuyer() != null) order.setBuyer(orderLogDTO.getBuyer());
		if(orderLogDTO.getSeller() != null) order.setSeller(orderLogDTO.getSeller());
		if(orderLogDTO.getQuantity() != null) order.setQuantity(orderLogDTO.getQuantity());
		if(orderLogDTO.getPrice() != null) order.setPrice(orderLogDTO.getPrice());
		if(orderLogDTO.getStatus() != null) order.setStatus(orderLogDTO.getStatus());
		if(orderLogDTO.getCreateTime() != null) order.setCreateTime(orderLogDTO.getCreateTime());
		if(orderLogDTO.getUpdateTime() != null) order.setUpdateTime(orderLogDTO.getUpdateTime());
		if(orderLogDTO.getBuy() != null) order.setBuy(orderLogDTO.getBuy());
		if(orderLogDTO.getSell() != null) order.setSell(orderLogDTO.getSell());
		if(orderLogDTO.getGameItem() != null) order.setGameItem(orderLogDTO.getGameItem());
		return order;
	}
}
