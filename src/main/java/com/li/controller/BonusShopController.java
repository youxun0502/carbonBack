package com.li.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.li.model.BonusItem;
import com.li.service.BonusService;

@Controller
public class BonusShopController {

	@Autowired
	private BonusService bService;
	
	@GetMapping("/bonus-shop/main")
	public String goBackToBonusshop(Model model) {
		List<BonusItem> list = bService.findAll();
		model.addAttribute("bonusitemList", list);
		return "li/bonusshop";
	}
	@GetMapping("/profile")
	public String goBackToProfile(Model model) {
		List<BonusItem> list = bService.findAll();
		model.addAttribute("bonusitemList", list);
		return "li/profile";
	}
	
}
