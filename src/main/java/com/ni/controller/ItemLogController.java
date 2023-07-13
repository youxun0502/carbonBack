package com.ni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ni.dto.ItemLogDTO;
import com.ni.model.ItemLog;
import com.ni.service.ItemLogService;

@Controller
public class ItemLogController {

	@Autowired
	private ItemLogService itemLogService;
	
	@GetMapping("/gameitem/allItemLog")
	public String getAllItemLog(Model m) {
		List<ItemLogDTO> logs = itemLogService.findAll();
		m.addAttribute("logs", logs);
		return "ni/itemLogDataTable";
	}
	
	@GetMapping("/gameitem/newItemLog")
	public String insertPage() {
		return "ni/itemLogInsert";
	}
	
//	-------------- gameItemMarket -----------------------------
	@ResponseBody
	@PostMapping("/market/newItemLog")
	public ItemLog insert(@RequestBody ItemLogDTO itemLog) {
		return itemLogService.insert(itemLog);
	}
	
	@ResponseBody
	@GetMapping("/market/history")
	public Page<ItemLog> findOrderHistory(@RequestParam("memberId") Integer memberId, 
						@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		return itemLogService.findOrderHistory(memberId, pageNumber);
	}
	
//	-------------- profiles Inventory -----------------------------
	@GetMapping("/profile/{memberId}/inventory")
	public String myInventory(@PathVariable Integer memberId, Model m) {
		m.addAttribute("items", itemLogService.findByMemberId(memberId));
		return "ni/myInventory";
	}
	
	@GetMapping("/profile/orderHistory")
	public String myOrderHistory() {
		return "ni/myOrderHistory";
	}
	
	@ResponseBody
	@GetMapping("/profile/inventory/{memberId}")
	public List<ItemLogDTO> findByMemberId(@PathVariable Integer memberId) {
		return itemLogService.findByMemberId(memberId);
	}
}
