package com.ni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ni.dto.ItemOrderDTO;
import com.ni.model.GameItem;
import com.ni.model.ItemOrder;
import com.ni.service.GameItemService;
import com.ni.service.OrderService;

@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;
	@Autowired
	private GameItemService itemService;
	
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
		m.addAttribute("orders", orderService.findOrderList());
		return "ni/itemMarketList";
	}
	
	@GetMapping("/market/{gameId}/{itemName}")
	public String marketItem(@PathVariable Integer gameId, @PathVariable String itemName, Model m) {
		m.addAttribute("orders", orderService.findSellItemList(gameId, itemName));
//		show all item that it has any order 
//		change findSellItemList to findGameitemById and orderList will loading by ajax
		return "ni/itemMarketPage";
	}
	
	@ResponseBody
	@GetMapping("/market/orderLIst")
	public List<ItemOrderDTO> orderList(@PathVariable Integer gameId, @PathVariable String itemName) {
		return orderService.findSellItemList(gameId, itemName);
	}
	
	@ResponseBody
	@GetMapping("/market/buyAnItem")
	public ItemOrderDTO buyPage(@RequestParam("ordId") Integer ordId ,Model m) {
		m.addAttribute("order", orderService.findById(ordId));
		return orderService.findById(ordId);
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
	
	@GetMapping("/market/downloadImage/{itemId}")
	private ResponseEntity<byte[]> downloadImage(@PathVariable Integer itemId) {
		GameItem img1 = itemService.findById(itemId);
		byte[] itemImgFile = img1.getItemImg();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(itemImgFile, headers, HttpStatus.OK);
	}
}
