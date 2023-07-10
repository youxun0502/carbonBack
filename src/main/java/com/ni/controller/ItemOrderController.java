package com.ni.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.model.Member;
import com.liu.service.GmailService;
import com.liu.service.MemberService;
import com.ni.dto.ItemLogDTO;
import com.ni.dto.ItemOrderDTO;
import com.ni.model.GameItem;
import com.ni.model.ItemOrder;
import com.ni.service.GameItemService;
import com.ni.service.ItemLogService;
import com.ni.service.itemOrderService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

@Controller
public class ItemOrderController {

	@Autowired
	private itemOrderService orderService;
	@Autowired
	private GameItemService itemService;
	@Autowired
	private ItemLogService logService;
	@Autowired
	private GmailService gService;
	@Autowired
	private MemberService mService;
	
	@GetMapping("/gameitem/allOrder")
	public String getAllOrder(Model m) {
		List<ItemOrderDTO> orders = orderService.findAll();
		m.addAttribute("orders", orders);
		return "ni/orderDataTable";
	}
	@ResponseBody
	@GetMapping("/gameitem/api/allOrder")
	public List<ItemOrderDTO> getAllOrderAjax(Model m) {
		return orderService.findAll();
	}
	
	@ResponseBody
	@PutMapping("/gameitem/orderUpdate")
	public boolean update(@RequestBody ItemOrderDTO order) {
		ItemOrder result = orderService.updateStatusById(order.getOrdId(), order.getStatus());
		return result != null;
	}

	
//	----------------------------- gameItemMarket -----------------------------
	@GetMapping("/market")
	public String marketList(Model m) {
		m.addAttribute("orders", orderService.findMinPrice());
		m.addAttribute("items", itemService.findAll());
		return "ni/itemMarketList-gg";
	}
	
	@GetMapping("/market/{gameId}/{itemId}/{itemName}")
	public String marketItem(@PathVariable Integer gameId, @PathVariable String itemName, @PathVariable Integer itemId, 
							 @RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model m) {
		List<ItemOrderDTO> result = orderService.findSellItemList(gameId, itemName, pageNumber);
		if(result.isEmpty()) {
			m.addAttribute("item", itemService.findById(itemId));
			return "ni/itemMarketPage-gg";
		} 
		m.addAttribute("item", itemService.findById(itemId));
		m.addAttribute("orders", result);
//		改成ResponseBody? 
		return "ni/itemMarketPage-gg";
	}
	
	@ResponseBody
	@GetMapping("/market/orderLIst")
	public List<ItemOrderDTO> orderList(@PathVariable Integer gameId, @PathVariable String itemName, 
										@RequestParam(name = "p", defaultValue = "1") Integer pageNumber, Model m) {
		return orderService.findSellItemList(gameId, itemName, pageNumber);
	}
	
	@ResponseBody
	@GetMapping("/market/activeList")
	public List<ItemOrderDTO> findActiveList(@RequestParam("memberId") Integer memberId) {
		return orderService.findActiveList(memberId);
	}
	
	@ResponseBody
	@GetMapping("/market/buyAnItem")
	public ItemOrderDTO buyPage(@RequestParam("ordId") Integer ordId ,Model m) {
		return orderService.findById(ordId);
	}
	
	@ResponseBody
	@PostMapping("/market/done")
	public ItemOrderDTO buy(@RequestBody ItemOrderDTO orderDTO) throws AddressException, MessagingException, IOException {
		ItemOrder newOrder = orderService.insert(orderDTO);
		ItemOrderDTO orderInfo = orderService.findById(newOrder.getOrdId());
		
		ItemLogDTO logDTO = new ItemLogDTO();
		logDTO.setOrdId(orderInfo.getOrdId());
		logDTO.setItemId(orderInfo.getItemId());
		if(orderInfo.getBuyer() != null) {
			logDTO.setMemberId(orderInfo.getBuyer());
			logDTO.setQuantity(orderInfo.getQuantity());
			
			Member buyer = mService.findById(orderInfo.getBuyer());
			Member seller = mService.findById(orderInfo.getSeller());
			
			String url = "http://localhost:8080/carbon/profiles/inventory";
			gService.sendMessage(buyer.getEmail(), gService.getMyEmail(), "Carbon虛寶市集交易成功通知",
					"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + buyer.getUserId() + "您好:\n" + "\n" + 
					"感謝您此次於Carbon完成虛寶交易，點選以下連結前往個人頁面\n" + "\n" + url
					+ "\n\n" + "Carbon lys7744110@gmail.com");
			gService.sendMessage(seller.getEmail(), gService.getMyEmail(), "Carbon虛寶市集交易成功通知",
					"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + seller.getUserId() + "您好:\n" + "\n" + 
					"感謝您此次於Carbon完成虛寶交易，點選以下連結前往個人頁面\n" + "\n" + url
					+ "\n\n" + "Carbon lys7744110@gmail.com");
			
		} else {
			logDTO.setMemberId(orderInfo.getSeller());
			logDTO.setQuantity(Integer.parseInt(("-" + orderInfo.getQuantity())));
		}
		
		logDTO.setItemOrder(newOrder);
		logService.insert(logDTO);
		
		return orderInfo;
	}
	
	@ResponseBody
	@PostMapping("/market/newOrder")
	public ItemOrderDTO insert(@RequestBody ItemOrderDTO orderDTO) {
		ItemOrder newOrder = orderService.insert(orderDTO);
		return orderService.findById(newOrder.getOrdId());
	}
	
	@ResponseBody
	@PutMapping("/market/orderUpdate")
	public boolean updateStatus(@RequestBody ItemOrderDTO order) {
		orderService.updateStatusById(order.getOrdId(), order.getStatus());
		return true;
	}
	
	@ResponseBody
	@GetMapping("/market/itemPrices")
	public List<ItemOrderDTO> findByItemIdAndStatus(@RequestParam("itemId") Integer itemId) {
		return orderService.findByItemIdAndStatus(itemId);
	}
	
	@ResponseBody
	@GetMapping("/market/medianPrice")
	public List<Map<String, Object>> findMedianPrice(@RequestParam("itemId") Integer itemId) {
		return orderService.findMedianPrice(itemId);
	}
	
	@ResponseBody
	@GetMapping("/market/checkBuys")
	public List<ItemOrderDTO> checkBuysPrice(@RequestParam("itemId") Integer itemId) {
		return orderService.checkBuysPrice(itemId);
	}
	
	@ResponseBody
	@GetMapping("/market/checkSales")
	public List<ItemOrderDTO> checkSalesPrice(@RequestParam("itemId") Integer itemId) {
		return orderService.checkSalesPrice(itemId);
	}
	
	@GetMapping("/market/downloadImage/{itemId}")
	private ResponseEntity<byte[]> downloadImage(@PathVariable Integer itemId) {
		GameItem img1 = itemService.findById(itemId);
		byte[] itemImgFile = img1.getItemImg();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(itemImgFile, headers, HttpStatus.OK);
	}
}
