package com.ni.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ni.dto.ItemOrderDTO;
import com.ni.model.ItemOrder;
import com.ni.model.OrderRepository;

@Service
public class ItemOrderService {

	@Autowired
	private OrderRepository orderRepo;
	
	public List<ItemOrderDTO> findAll() {
		return convertToDTOList(orderRepo.findAll());
	}
	
	public Page<ItemOrder> findAllByPage(Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 6);
		return orderRepo.findAll(pgb);
	}
	
	public ItemOrderDTO findById(Integer ordId) {
		Optional<ItemOrder> optional = orderRepo.findById(ordId);
		if(optional.isPresent()) {
			return convertToDTO(optional.get());
		}
		return null;
	}
	
//	====================== 成交訂單 ======================
	public ItemOrder insert(ItemOrderDTO itemOrder) {
		if(itemOrder.getBuyer() != null) {
			itemOrder.setStatus(2);
		} else {
			itemOrder.setStatus(1);
		}
		ItemOrder newOrder = orderRepo.save(convertToOrder(itemOrder));
		return newOrder;
	}
	
//	====================== 上架訂單 ======================
	public ItemOrder insertOrder(ItemOrderDTO itemOrder) {
		itemOrder.setStatus(1);
		return orderRepo.save(convertToOrder(itemOrder));
	}
	
	@Transactional
	public ItemOrder updateStatusById(Integer ordId, Integer status) {
		Optional<ItemOrder> optional = orderRepo.findById(ordId);
		if(optional.isPresent()) {
			ItemOrder order = optional.get();
			order.setStatus(status);
			return order;
		}
		System.out.println("no update data");
		return null;
	}
	
	public List<ItemOrderDTO> findSellItemList(Integer gameId, String itemName) {
		return convertToDTOList(orderRepo.findSellItemList(gameId, itemName));
	}
	
	public List<Map<String, Object>> findOrderList() {
		List<Object[]> results = orderRepo.countOrderList();
	    List<Map<String, Object>> orderList = new ArrayList<>();
	    if(results != null) {
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
	    return null;
	}
	
	public List<Map<String, Object>> findMinPrice() {
		List<Object[]> results = orderRepo.findMinPrice();
		List<Map<String, Object>> priceList = new ArrayList<>();
		if(results != null) {
			for(Object[] result : results) {
				Map<String, Object> price = new HashMap<>();
				price.put("itemId", result[0]);
				price.put("minPrice", result[1]);
				priceList.add(price);
			}
			return priceList;
		}
		return null;
	}
	
	public List<ItemOrderDTO> findByItemIdAndStatus(Integer itemId) {
		return convertToDTOList(orderRepo.findByItemIdAndStatus(itemId));
	}
	
	public List<Map<String, Object>> findMedianPrice(Integer itemId) {
		List<Object[]> results = orderRepo.findMedianPrice(itemId);
	    List<Map<String, Object>> medianPriceList = new ArrayList<>();
	    if(results != null) {
	    	for (Object[] result : results) {
	    		Map<String, Object> medianPrice = new HashMap<>();
	    		medianPrice.put("time", result[0] + ":00:00");
	    		medianPrice.put("itemId", result[1]);
	    		medianPrice.put("medianPrice", result[2]);
	    		medianPrice.put("total", result[3]);
	    		medianPriceList.add(medianPrice);
	    	}
	    	return medianPriceList;
	    }
	    return null;
	}
	
	public List<ItemOrderDTO> checkBuysPrice(Integer itemId) {
		return convertToDTOList(orderRepo.findBuysByIdAndStatus(itemId));
	}
	
	public List<ItemOrderDTO> checkSalesPrice(Integer itemId) {
		return convertToDTOList(orderRepo.findSalesByIdAndStatus(itemId));
	}
	
	public Page<ItemOrder> findBuyOrder(Integer memberId, Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 5);
		return orderRepo.findBuyOrder(memberId, pgb);
	}
	
	public Page<ItemOrder> findSaleList(Integer memberId, Integer pageNumber) {
		Pageable pgb = PageRequest.of(pageNumber - 1, 5);
		return orderRepo.findSaleList(memberId, pgb);
	}
	
//	======================= 轉換 DTO 和 Entity =======================
	public List<ItemOrderDTO> convertToDTOList(List<ItemOrder> orders) {
		List<ItemOrderDTO> orderDTOList = new ArrayList<>();
		for(ItemOrder order : orders) {
			ItemOrderDTO orderDTO = new ItemOrderDTO();
			if(order.getOrdId() != null) orderDTO.setOrdId(order.getOrdId());
			if(order.getItemId() != null) orderDTO.setItemId(order.getItemId());
			if(order.getBuyer() != null) orderDTO.setBuyer(order.getBuyer());
			if(order.getSeller() != null) orderDTO.setSeller(order.getSeller());
			if(order.getQuantity() != null) orderDTO.setQuantity(order.getQuantity());
			if(order.getPrice() != null) orderDTO.setPrice(order.getPrice());
			if(order.getStatus() != null) orderDTO.setStatus(order.getStatus());
			if(order.getCreateTime() != null) orderDTO.setCreateTime(order.getCreateTime());
			if(order.getUpdateTime() != null) orderDTO.setUpdateTime(order.getUpdateTime());
			if(order.getBuy() != null) orderDTO.setBuy(order.getBuy());
			if(order.getSell() != null) orderDTO.setSell(order.getSell());
			if(order.getGameItem() != null) orderDTO.setGameItem(order.getGameItem());
			orderDTOList.add(orderDTO);
		}
		return orderDTOList;
	}
	
	public ItemOrderDTO convertToDTO(ItemOrder order) {
		ItemOrderDTO orderDTO = new ItemOrderDTO();
		if(order.getOrdId() != null) orderDTO.setOrdId(order.getOrdId());
		if(order.getItemId() != null) orderDTO.setItemId(order.getItemId());
		if(order.getBuyer() != null) orderDTO.setBuyer(order.getBuyer());
		if(order.getSeller() != null) orderDTO.setSeller(order.getSeller());
		if(order.getQuantity() != null) orderDTO.setQuantity(order.getQuantity());
		if(order.getPrice() != null) orderDTO.setPrice(order.getPrice());
		if(order.getStatus() != null) orderDTO.setStatus(order.getStatus());
		if(order.getCreateTime() != null) orderDTO.setCreateTime(order.getCreateTime());
		if(order.getUpdateTime() != null) orderDTO.setUpdateTime(order.getUpdateTime());
		if(order.getBuy() != null) orderDTO.setBuy(order.getBuy());
		if(order.getSell() != null) orderDTO.setSell(order.getSell());
		if(order.getGameItem() != null) orderDTO.setGameItem(order.getGameItem());
		return orderDTO;
	}
	
	public ItemOrder convertToOrder(ItemOrderDTO orderDTO) {
		ItemOrder order = new ItemOrder();
		if(orderDTO.getOrdId() != null) order.setOrdId(orderDTO.getOrdId());
		if(orderDTO.getItemId() != null) order.setItemId(orderDTO.getItemId());
		if(orderDTO.getBuyer() != null) order.setBuyer(orderDTO.getBuyer());
		if(orderDTO.getSeller() != null) order.setSeller(orderDTO.getSeller());
		if(orderDTO.getQuantity() != null) order.setQuantity(orderDTO.getQuantity());
		if(orderDTO.getPrice() != null) order.setPrice(orderDTO.getPrice());
		if(orderDTO.getStatus() != null) order.setStatus(orderDTO.getStatus());
		if(orderDTO.getCreateTime() != null) order.setCreateTime(orderDTO.getCreateTime());
		if(orderDTO.getUpdateTime() != null) order.setUpdateTime(orderDTO.getUpdateTime());
		if(orderDTO.getBuy() != null) order.setBuy(orderDTO.getBuy());
		if(orderDTO.getSell() != null) order.setSell(orderDTO.getSell());
		if(orderDTO.getGameItem() != null) order.setGameItem(orderDTO.getGameItem());
		return order;
	}
}
