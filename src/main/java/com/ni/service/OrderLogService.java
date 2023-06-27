package com.ni.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ni.model.OrderLog;
import com.ni.model.OrderLogRepository;

@Service
public class OrderLogService {

	@Autowired
	private OrderLogRepository orderRepo;
	
	public List<OrderLog> findAll() {
		return orderRepo.findAll();
	}
	
	public OrderLog findById(Integer logId) {
		Optional<OrderLog> optional = orderRepo.findById(logId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public List<OrderLog> findByBuyerId(Integer buyer) {
		return null;
	}
	
	public List<OrderLog> findBysellerId(Integer seller) {
		return null;
	}
	
	public OrderLog insert(OrderLog orderLog) {
		return orderRepo.save(orderLog);
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
	
	public List<OrderLog> findSellItemList(Integer gameId, String itemName) {
		return orderRepo.findSellItemList(gameId, itemName);
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
}
