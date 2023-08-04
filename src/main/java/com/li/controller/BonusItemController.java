package com.li.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.li.dto.BonusPointDto;
import com.li.dto.BonusShopDto;
import com.li.model.BonusItem;
import com.li.service.BonusLogService;
import com.li.service.BonusPointService;
import com.li.service.BonusService;
import com.liu.service.MemberService;

@Controller
public class BonusItemController {

	@Autowired
	private BonusService bService;
	
	@Autowired
	private BonusLogService blService;
	
	@Autowired
	private BonusPointService bpService;
	
	@Autowired MemberService mService;

	@GetMapping("bonus/main")
	public String goBackToBounusMain(Model model) {
		List<BonusItem> list = bService.findAll();
		model.addAttribute("bonusitemList", list);
		return "redirect:/bonus/listAll";
	}
	@GetMapping("bonus/bonuslog")
	public String gotoBonusLogPage(Model model) {
		List<BonusShopDto> listDto=blService.findAlltoDto();
		model.addAttribute("Dtos",listDto);
		return "li/listBonusLog" ;
	}
	@GetMapping("bonus/bonuspointlog")
	public String gotoBonusPointLogPage(Model model) {
		List<BonusPointDto> listDto = bpService.findAlltoDto();
		model.addAttribute("Dtos",listDto);
		return "li/listBonusPoint" ;
	}
	
	@ResponseBody
	@GetMapping("/bonus/findmemberpoint")
	public List<BonusPointDto> getmemberpoint(@RequestParam("bonus_search") String str, Model model) {
//		List<BonusItem> result = bService.findByName(str);
//		model.addAttribute("bonusitemList", result);
//		return "li/listAll";
		
		return bpService.findByMemberIdtoDto(Integer.parseInt(str));
	}
	
	@GetMapping("bonus/insert")
	public String gotoBonusInsert() {
		return "li/Insert";
	}

	@PostMapping("bonus/upload")
	public String InsertBonusItemToDB(@RequestParam("bonusName") String bonusname,
			@RequestParam("bonusPrice") Integer bonusPrice, @RequestParam("bonusDes") String bonusDes,
			@RequestParam("status")Boolean status,
			@RequestParam("bonusType")String bonusType,
			@RequestParam("img_file") MultipartFile file) {

		try {
			BonusItem bonusItem = new BonusItem();
			bonusItem.setBonusName(bonusname);
			bonusItem.setBonusPrice(bonusPrice);
			bonusItem.setBonusDes(bonusDes);
			bonusItem.setImg_file(file.getBytes());
			bonusItem.setBonusImg(bonusname);
			bonusItem.setStatus(status);
			bonusItem.setBonusType(bonusType);
			bService.insertBonusItem(bonusItem);
			
			return "redirect:/bonus/listAll";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/bonus/main";
		}
	}
	
	@GetMapping("/bonus/listAll")
	public String getAllBonusItem(Model model) {
		List<BonusItem> list = bService.findAll();
		model.addAttribute("bonusitemList", list);
		return "li/listAll";
	}
	
	@GetMapping("/downloadImage/{id}")
	public ResponseEntity<byte[]> downloadImage(@PathVariable Integer id){
		BonusItem photo1 = bService.getBonusItemById(id);
		byte[] photoFile = photo1.getImg_file();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		                                // 檔案, header, HttpStatus
		return new ResponseEntity<byte[]>(photoFile, headers, HttpStatus.OK);
	}
	
	@GetMapping("/bonus/edit")
	public String editPage(@RequestParam("id") Integer id, Model model) {
		BonusItem messages = bService.getBonusItemById(id);
		model.addAttribute("bonusitem", messages);
		return "li/edit";
	}
	@PutMapping("/bonus/edit")
	public String editPost(@ModelAttribute(name="bonusitem") BonusItem bi) {
		bService.updateBonusItemById(bi.getBonusId(),bi.getBonusName(),bi.getBonusPrice(),bi.getBonusDes(),bi.isStatus(),bi.getBonusType());
		
		return "redirect:/bonus/listAll";
	}
	
	@ResponseBody
	@DeleteMapping("bonus/delete")
	public String deletePost(@RequestParam("id") Integer id,Model model) {		
		bService.deleteById(id);
		
		return "redirect:/bonus/listAll";
	}
	
	@ResponseBody
	@GetMapping("/bonus/findLikeByName")
	public List<BonusItem> getBonusItemByName(@RequestParam("bonus_search") String str, Model model) {
//		List<BonusItem> result = bService.findByName(str);
//		model.addAttribute("bonusitemList", result);
//		return "li/listAll";
		return bService.findByName(str);
	}
	
	@GetMapping("bonus/search")
	public String searchBonusItemByName(@RequestParam("bonusitem_search") String str, Model model) {
		List<BonusItem> result = bService.findByName(str);
		model.addAttribute("bonusitemList", result);
		return "li/listAll";
	}
	

}
