package com.li.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.li.dto.BonusShopDto;
import com.li.model.BonusItem;
import com.li.model.BonusLog;
import com.li.service.BonusLogService;
import com.li.service.BonusPointService;
import com.li.service.BonusService;

@Controller
public class BonusShopController {

	@Autowired
	private BonusService bService;
	@Autowired
	private BonusLogService blService;
	@Autowired
	private BonusPointService bpService;

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

	@ResponseBody
	@GetMapping("/bonus-shop/api/getbonusitem")
	public BonusShopDto getUpdateData(@RequestParam(name = "id") Integer id) {
		BonusItem oneData = bService.getBonusItemById(id);
		if (oneData != null) {
			BonusShopDto oneDto = new BonusShopDto();
			oneDto.setBonusId(oneData.getBonusId());
			oneDto.setBonusName(oneData.getBonusName());
			oneDto.setBonusprice(oneData.getBonusPrice());
			oneDto.setBonusDes(oneData.getBonusDes());
			oneDto.setBonusType(oneData.getBonusType());
			return oneDto;
		} else {
			return null;
		}
	}
	
	@ResponseBody
	@PostMapping("/bonus-shop/api/buybonusitem")
	public BonusShopDto buyBonusItem(@RequestBody BonusShopDto bonusshopDto,Model model) {
//		System.out.println(bonusshopDto.getBonusId());
//		System.out.println(bonusshopDto.getMemberId());
//		System.out.println(bonusshopDto.getBonusprice());
//		System.out.println(bonusshopDto.getPoint());
//		BonusItem bitem = bService.getBonusItemById(bonusshopDto.getBonusId());
		BonusLog bLog = new BonusLog();
		bLog.setBonusId(bonusshopDto.getBonusId());
		bLog.setMemberId(bonusshopDto.getMemberId());
		blService.newBonusLog(bLog);
		
		bpService.newPointLog("buybonusitem", bonusshopDto.getMemberId(), -(bonusshopDto.getBonusprice()));
//		System.out.println(-(bonusshopDto.getBonusprice()));
		
		return bonusshopDto;
	}
	
	@ResponseBody
	@PostMapping("/bonus-shop/api/findBonusLog")
	public List<BonusShopDto> findBonusLogByMemberid(@RequestBody BonusShopDto bonusshopDto,Model model) {
//		System.out.println("ININININININININININININININ!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//		System.out.println(bonusshopDto.getMemberId());

//	blService.findByMemberIdtoDto(bonusshopDto.getMemberId());
	return blService.findByMemberIdtoDto(bonusshopDto.getMemberId());
	}

}
