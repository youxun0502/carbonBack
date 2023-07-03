package com.li.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.li.dto.BonusShopDto;
import com.li.model.BonusItem;
import com.li.model.BonusPointLog;
import com.li.service.BonusPointService;

@Controller
public class BonusPointController {

	@Autowired
	private BonusPointService bpService;
	
	@ResponseBody
	@GetMapping("/bonus-shop/api/point")
	public BonusShopDto getPoint(@RequestParam(name = "id") Integer id) {
		BonusPointLog theLastPoint = bpService.theLastPoint(id);
		if (theLastPoint != null) {
			BonusShopDto oneDto = new BonusShopDto();
			oneDto.setMemberId(id);
			oneDto.setPoint(theLastPoint.getPoint());
			return oneDto;
		} else {
			return null;
		}
	}
	
}
