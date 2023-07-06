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

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
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

	//綠界
	public String ecpayCheckout() {

		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		
		obj.setMerchantTradeNo("0975667900testCompany0004");
		obj.setMerchantTradeDate("2017/01/01 08:05:23");
		obj.setTotalAmount("50");
		obj.setTradeDesc("test Description");
		obj.setItemName("TestItem");
		// 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok	
		obj.setReturnURL("http://127.0.0.1:4040");
		obj.setNeedExtraPaidInfo("N");
		// 商店轉跳網址 (Optional)
		obj.setClientBackURL("http://192.168.1.37:8080/");
		String form = all.aioCheckOut(obj, null);

		return form;
	}
}
