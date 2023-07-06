package com.evan.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.google.api.services.gmail.Gmail.Users.Drafts.Update;
import com.liu.model.Member;
import com.liu.model.MemberRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
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
	public String ecpayCheckout(Map<String, Object> formData) {

		AllInOne all = new AllInOne("");

		AioCheckOutALL obj = new AioCheckOutALL();
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		
		Date payTime = new Date();
        String dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(payTime);
		formData.get("logs");
		System.out.println(formData.get("logs"));
		
		
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate(dateFormat);
		obj.setTotalAmount((String)formData.get("totalSum"));
		obj.setTradeDesc((String)formData.get("logs"));
		obj.setItemName("Carbon Game");
		// 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok	
		obj.setReturnURL("http://127.0.0.1:4040");
		obj.setNeedExtraPaidInfo("N");
		// 商店轉跳網址 (Optional)
		obj.setClientBackURL("http://localhost:8080/carbon/gameFront/order/ecpaystatus?uuid="+uuId+"&memberId="+formData.get("memberId")+"&orderId="+formData.get("orderId"));
		String form = all.aioCheckOut(obj, null);

		return form;
	}

	public boolean ecpayTradingStatus(Map<String, Object> formData) {
		AllInOne all = new AllInOne("");
		QueryTradeInfoObj obj = new QueryTradeInfoObj();
		obj.setMerchantTradeNo((String)formData.get("uuid"));
		String queryTradeInfo = all.queryTradeInfo(obj);
		System.out.println(queryTradeInfo);
		
		String tradeStatus = null;
		
		String[] keyvaluePair = queryTradeInfo.split("&");
		for (String pair : keyvaluePair) {
			String[] keyValue = pair.split("=");
            if (keyValue.length == 2 && keyValue[0].equals("TradeStatus")) {
                tradeStatus = keyValue[1];
                break;
            }
		}
		
		if(Integer.parseInt(tradeStatus) == 1) {
			UpdategameOrderToSuccess(Integer.parseInt((String)formData.get("orderId")));
			return true;
		}else {
			return false;
		}
		
	}

	@Transactional
	private void UpdategameOrderToSuccess(int orderId) {
		GameOrder gameOrder = goRepos.findById(orderId).orElse(null);
		gameOrder.setStatus(1);
		goRepos.save(gameOrder);
	}
	
	
}
