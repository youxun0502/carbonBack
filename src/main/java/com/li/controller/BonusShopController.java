package com.li.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.li.dto.BonusShopDto;
import com.li.model.BonusItem;
import com.li.service.BonusService;
import com.liu.dto.MemberDto;
import com.liu.model.Member;

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
			return oneDto;
		} else {
			return null;
		}
	}
	
	@ResponseBody
	@PostMapping("bonus/newBonusLog")
	public String InsertBonusItemToDB(@RequestParam("bonusName") String bonusname,
			@RequestParam("bonusPrice") Integer bonusPrice, @RequestParam("bonusDes") String bonusDes,
			@RequestParam("status")Boolean status,
			@RequestParam("img_file") MultipartFile file) {

		try {
			BonusItem bonusItem = new BonusItem();
			bonusItem.setBonusName(bonusname);
			bonusItem.setBonusPrice(bonusPrice);
			bonusItem.setBonusDes(bonusDes);
			bonusItem.setImg_file(file.getBytes());
			bonusItem.setBonusImg(bonusname);
			bonusItem.setStatus(status);
			
			bService.insertBonusItem(bonusItem);
			
			return "redirect:/bonus/listAll";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/bonus/main";
		}
	}

}
