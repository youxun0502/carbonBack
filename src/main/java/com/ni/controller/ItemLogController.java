package com.ni.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ni.model.ItemLog;
import com.ni.service.ItemLogService;

@Controller
public class ItemLogController {

	@Autowired
	private ItemLogService itemLogService;
	
	@GetMapping("/gameitem/allItemLog")
	public String getAllItemLog(Model m) {
		List<ItemLog> logs = itemLogService.findAll();
		m.addAttribute("logs", logs);
		return "ni/itemLogDataTable";
	}
	
	@GetMapping("/gameitem/itemLogUpdate")
	public String updatePage() {
		return "ni/itemLogUpdate";
	}
	
	@PutMapping("/gameitem/itemLogUpdate")
	public String update(@RequestBody ItemLog itemLog) {
		itemLogService.findById(itemLog.getId());
		itemLogService.updateById(itemLog);
		return "redirect:/gameitem/allItemLog";
	}
	
	@GetMapping("/gameitem/newItemLog")
	public String insertPage() {
		return "ni/itemLogInsert";
	}
	
	@ResponseBody
	@DeleteMapping("/gameitem/itemLogDelete")
	public String deleteAjax(@RequestParam("id") Integer id) {
		itemLogService.delete(id);
		return "delete OK!";
	}

	
//	-------------- gameItemMarket -----------------------------
	@ResponseBody
	@PostMapping("/market/newItemLog")
	public ItemLog insert(@RequestBody ItemLog itemLog) {
		return itemLogService.insert(itemLog);
	}
	
}
