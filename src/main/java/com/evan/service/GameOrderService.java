package com.evan.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import com.evan.dao.GameOrderRepository;
import com.evan.dao.GameRepository;
import com.evan.dto.CartDTO;
import com.evan.dto.OrderDTO;
import com.evan.dto.OrderLogDTO;
import com.evan.model.Game;
import com.evan.model.GameOrder;
import com.evan.model.GameOrderLog;
import com.evan.utils.ConvertToDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.gmail.Gmail.Users.Drafts.Update;
import com.li.service.BonusPointService;
import com.liu.model.Member;
import com.liu.model.MemberRepository;
import com.liu.service.GmailService;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.QueryTradeInfoObj;
import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Order;

@Service
public class GameOrderService {


	@Autowired
	private MemberRepository mRepos;
	@Autowired
	private GameOrderRepository goRepos;
	@Autowired
	private GameRepository gRepos;
	@Autowired
	private CartService cService;
	@Autowired 
	private GmailService gmailService;
	@Autowired
	private BonusPointService bpService;

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

	//綠界金流方法
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

	//確認訂單狀態
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
			Member member = mRepos.findById(Integer.parseInt((String)formData.get("memberId"))).orElse(null);
			try {
				gmailService.sendMessage(member.getEmail(),gmailService.getMyEmail(),"訂單編號：\""+(String)formData.get("orderId")+"\"交易結果通知信"
						,"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + member.getMemberName() + "您好:\n" + "\n" + 
						 "感謝您此次於Carbon完成訂單交易" + "\n" 
						 + "\n\n" + "Carbon lys7744110@gmail.com");
			} catch (MessagingException | IOException e) {
				e.printStackTrace();
			}
			return true;
		}else {
			return false;
		}

	}

	@Transactional
	private void UpdategameOrderToSuccess(int orderId) {
		GameOrder gameOrder = goRepos.findById(orderId).orElse(null);
		Integer id = gameOrder.getMember().getId();
		Integer orderPrice=0;
		for (GameOrderLog orderLog : gameOrder.getGameOrderLog()) {
			Game game = gRepos.findGameByGameName(orderLog.getGameName()).get(0);
			orderPrice += game.getPrice();
			Integer oldCount = game.getBuyerCount();
			game.setBuyerCount(oldCount+1);
			gRepos.save(game);
		}
//		bpService.newPointLog("buygame", id, orderPrice);
		gameOrder.setStatus(1);
		goRepos.save(gameOrder);
	}

	public RedirectView linePayFirstRequest(Map<String, Object> formData) {
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);

		// 構建請求參數
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("amount", (String)formData.get("totalSum"));
		requestBody.put("productName", (String)formData.get("logs"));
		requestBody.put("confirmUrl", "http://localhost:8080/carbon/gameFront/order/linePay?orderId="
				+(String)formData.get("orderId")+"&memberId="
				+(String)formData.get("memberId")+"&totalSum="
				+(String)formData.get("totalSum"));
		requestBody.put("orderId", uuId);
		requestBody.put("currency", "TWD");

		// 構建Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-LINE-ChannelId", "2000063549");
		headers.add("X-LINE-ChannelSecret", "2097997c3d54c6fafc5d7746b962975b");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody;
		try {
			jsonBody = objectMapper.writeValueAsString(requestBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new RedirectView("/");
		}

		// 建構請求實體
		HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);

		// 發送請求
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange("https://sandbox-api-pay.line.me/v2/payments/request",
				HttpMethod.POST, httpEntity, String.class);

		// 處理響應
		if (response.getStatusCode().is2xxSuccessful()) {
			// 解析響應
			ObjectMapper objectMapper1 = new ObjectMapper();
			try {
				JsonNode responseJson = objectMapper1.readTree(response.getBody());
				String paymentWebUrl = responseJson.get("info").get("paymentUrl").get("web").asText();
				// 將 paymentWebUrl 返回給前端進行跳轉
				return new RedirectView(paymentWebUrl);
			} catch (IOException e) {
				e.printStackTrace();
				return new RedirectView("/");
			}
		} else {
			//付款失敗
			return new RedirectView("/");

		}
	}
	public boolean linePaySecondRequest(Map<String, Object> formData) {

		// 構建請求參數
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("amount", (String)formData.get("totalSum"));
		requestBody.put("currency", "TWD");

		// 構建Header
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-LINE-ChannelId", "2000063549");
		headers.add("X-LINE-ChannelSecret", "2097997c3d54c6fafc5d7746b962975b");

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonBody;
		try {
			jsonBody = objectMapper.writeValueAsString(requestBody);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		}

		// 建構請求實體
		HttpEntity<String> httpEntity = new HttpEntity<>(jsonBody, headers);

		// 發送請求
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange("https://sandbox-api-pay.line.me/v2/payments/"
				+ (String)formData.get("transactionId")+"/confirm",
				HttpMethod.POST, httpEntity, String.class);

		// 處理響應
		if (response.getStatusCode().is2xxSuccessful()) {
			// 解析響應
			ObjectMapper objectMapper1 = new ObjectMapper();
			try {
				JsonNode responseJson = objectMapper1.readTree(response.getBody());
				String result = responseJson.get("returnMessage").asText();
				// 將 paymentWebUrl 返回給前端進行跳轉
				System.out.println(result);
				if("Success.".equals(result)) {
					UpdategameOrderToSuccess(Integer.parseInt((String)formData.get("orderId")));
					Member member = mRepos.findById(Integer.parseInt((String)formData.get("memberId"))).orElse(null);
					gmailService.sendMessage(member.getEmail(),gmailService.getMyEmail(),"訂單編號：\""+(String)formData.get("orderId")+"\"交易結果通知信"
							,"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + member.getMemberName() + "您好:\n" + "\n" + 
							 "感謝您此次於Carbon完成訂單交易" + "\n" 
							 + "\n\n" + "Carbon lys7744110@gmail.com");
					
					}
				return "Success.".equals(result);
			} catch (IOException | MessagingException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			//付款失敗
			return false;

		}
	}

	//尋找使用者已經擁有的遊戲
	public List<OrderLogDTO> getMemberOwnGames(Map<String, Object> formData) {
		int memberId = Integer.parseInt((String) formData.get("memberId"));
		System.out.println(memberId);
		Member member = mRepos.findById(memberId).orElse(null);
		System.out.println("step1");

		Set<String> gameNames = new HashSet<>();
		List<OrderLogDTO> successOrder = new ArrayList<>();

		for (OrderDTO orderDTO : cdDTO.outputOrderDTOList(member.getGameOrder())) {
			if ("已付款".equals(orderDTO.getStatus())) {
				for (OrderLogDTO orderLogDTO : orderDTO.getLogs()) {
					String gameName = orderLogDTO.getGameName();
					System.out.println(orderLogDTO.getPhotoId());
					if (!gameNames.contains(gameName)) {
						gameNames.add(gameName);
						successOrder.add(orderLogDTO);
					}
				}
			}
		}

		return successOrder;
	}



}
