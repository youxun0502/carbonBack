package com.evan.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evan.dao.GameOrderRepository;
import com.evan.dto.CartDTO;
import com.evan.dto.OrderDTO;
import com.evan.model.GameOrder;
import com.evan.model.GameOrderLog;
import com.evan.utils.ConvertToDTO;
import com.liu.model.Member;
import com.liu.model.MemberRepository;

import jakarta.persistence.criteria.Order;

@Service
public class GameOrderService {

	
	@Autowired
	private MemberRepository mRepos;
	@Autowired
	private GameOrderRepository goRepos;
	
	@Autowired
	private CartService cService;
	
	@Autowired
	private ConvertToDTO cdDTO;
	
//	@Transactional
	public void addOrder(Map<String, Object> formData) {
		int memberId = Integer.parseInt((String)(formData.get("memberId")));
		List<CartDTO> memberCart = cService.getMemberCart(memberId);
		List<GameOrderLog> logs = new ArrayList<GameOrderLog>();
		Member member = mRepos.findById(memberId).orElse(null);
		
		GameOrder gameOrder = new GameOrder();
		for (CartDTO cart : memberCart) {
			GameOrderLog gameOrderLog = new GameOrderLog();
			gameOrderLog.setGameName(cart.getGameName());
			gameOrderLog.setPriceLog(cart.getPrice());
			gameOrderLog.setGameOrder(gameOrder);
			logs.add(gameOrderLog);
		}
//		List<GameOrder> orderList = new ArrayList<>();
		gameOrder.setStatus(0);
		gameOrder.setMember(member);
		gameOrder.setGameOrderLog(logs);
		
//		orderList.add(gameOrder);
//		member.setGameOrder(orderList);
//		member.getGameOrder().add(gameOrder);
		
		goRepos.save(gameOrder);
	}
	
	public List<OrderDTO> getOrders(Map<String, Object> formData) {
		int memberId = Integer.parseInt((String)(formData.get("memberId")));
		Member member = mRepos.findById(memberId).orElse(null);
		List<OrderDTO> outputOrderDTOList = cdDTO.outputOrderDTOList(member.getGameOrder());
		Collections.reverse(outputOrderDTOList);
		return outputOrderDTOList;
	
	}

	public void deleteOrder(Map<String, Object> formData) {
		int orderId = Integer.parseInt((String)(formData.get("orderId")));
		goRepos.deleteById(orderId);
	}
}
