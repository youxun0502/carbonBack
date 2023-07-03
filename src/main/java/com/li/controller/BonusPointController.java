package com.li.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.li.dto.BonusShopDto;
import com.li.service.BonusPointService;

@Controller
public class BonusPointController {

	@Autowired
	private BonusPointService bpService;
	
	@GetMapping("/bonus-shop/point")
	public BonusShopDto getPoint() {
		return null;
	}
	
}
