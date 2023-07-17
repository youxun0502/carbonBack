package com.li.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.li.dto.BonusShopDto;
import com.li.dto.EditAvatarDto;
import com.li.model.BonusItem;
import com.li.model.BonusLog;
import com.li.service.BonusLogService;
import com.li.service.BonusPointService;
import com.li.service.BonusService;
import com.liu.model.Member;
import com.liu.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BonusShopController {

	@Autowired
	private BonusService bService;
	@Autowired
	private BonusLogService blService;
	@Autowired
	private BonusPointService bpService;
	@Autowired
	private MemberService mService;

	@GetMapping("/bonus-shop/main")
	public String goBackToBonusshop(Model model) {
		List<BonusItem> list = bService.findAll();
		model.addAttribute("bonusitemList", list);
		return "li/bonusshop";
	}
	
	@GetMapping("/bonus-shop/allAvatar")
	public String gotoAllAvatar(Model model) {
		List<BonusItem> list = bService.findAllAvatar();
		model.addAttribute("bonusitemList", list);
		return "li/fullAV";
	}
	@GetMapping("/bonus-shop/allFrame")
	public String gotoAllFrame(Model model) {
		List<BonusItem> list = bService.findAllFrame();
		System.out.println("list: "+list.get(0).getBonusName());
		model.addAttribute("bonusitemList", list);
		return "li/fullAV";
	}
	@GetMapping("/bonus-shop/allBackground")
	public String gotoAllBackground(Model model) {
		List<BonusItem> list = bService.findAllBackground();
		model.addAttribute("bonusitemList", list);
		return "li/fullAV";
	}

	@GetMapping("/profile")
	public String goBackToProfile(Model model,HttpSession session) {
		Member member =(Member)session.getAttribute("memberBeans");
		session.setAttribute("memberBeans", mService.findById(member.getId()));
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
	public BonusShopDto buyBonusItem(@RequestBody BonusShopDto bonusshopDto, Model model) {

		BonusLog bLog = new BonusLog();
		bLog.setBonusId(bonusshopDto.getBonusId());
		bLog.setMemberId(bonusshopDto.getMemberId());
		blService.newBonusLog(bLog);

		bpService.newPointLog("buybonusitem", bonusshopDto.getMemberId(), -(bonusshopDto.getBonusprice()));

		return bonusshopDto;
	}
	
	@ResponseBody
	@PostMapping("/bonus-shop/api/checkbought")
	public BonusShopDto checkBonusLog(@RequestBody BonusShopDto bonusshopDto, Model model) {
		bonusshopDto.setIsBought(blService.isBuy(bonusshopDto.getMemberId(), bonusshopDto.getBonusId()));
		return bonusshopDto;
	}

	@ResponseBody
	@PostMapping("/bonus-shop/api/findBonusLog")
	public List<BonusShopDto> findBonusLogByMemberid(@RequestBody BonusShopDto bonusshopDto, Model model) {
		return blService.findByMemberIdtoDto(bonusshopDto.getMemberId());
	}

	@PostMapping("/bonus-shop/api/selectEdit")
	public String selectAvatarEdit(HttpSession sesstion,
			@RequestParam("the-new-avatar")Integer avatar,
			@RequestParam("the-new-frame")Integer frame,
			@RequestParam("the-new-bg")Integer background
			) {
		Member member =(Member) sesstion.getAttribute("memberBeans");
		mService.updateAvatar(member.getId(), avatar, frame, background);
		
		return "redirect:/memberFront/memberInformationPage";
	}
	
	@ResponseBody
	@GetMapping("/bonus-shop/avatar-page")
	public Page<BonusItem> bonusshopAvatarListPage(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		return bService.findByBonusTypePage("avatar", pageNumber);		
	}
	@ResponseBody
	@GetMapping("/bonus-shop/frame-page")
	public Page<BonusItem> bonusshopFrameListPage(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		return bService.findByBonusTypePage("frame", pageNumber);		
	}
	@ResponseBody
	@GetMapping("/bonus-shop/bg-page")
	public Page<BonusItem> bonusshopBgListPage(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		return bService.findByBonusTypePage("background", pageNumber);		
	}

}
