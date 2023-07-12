package com.ni.controller;

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

import com.evan.model.Game;
import com.ni.model.GameItem;
import com.ni.service.GameItemService;

@Controller
public class GameItemController {

	@Autowired
	private GameItemService itemService;
	
	@GetMapping("/gameitem")
	public String GameItemMain() {
		return "ni/itemMain";
	}
	
	@GetMapping("/gameitem/allGameitem")
	public String getAllItem(Model m) {
		List<GameItem> items = itemService.findAll();
		m.addAttribute("items", items);
		return "ni/itemDataTable";
	}
	
	@ResponseBody
	@GetMapping("/gameitem/api/allGameitem")
	public List<GameItem> getAllItemAjax() {
		List<GameItem> items = itemService.findAll();
		return items;
	}
	
	@GetMapping("/gameitem/getGameitemByName")
	public String getItemByName(@RequestParam("itemName") String itemName, Model m) {
		m.addAttribute("items", itemService.findByLikeName(itemName));
		return "ni/itemDataTable";
	}
	
	@ResponseBody
	@GetMapping("/gameitem/api/getGameitemByName")
	public List<GameItem> getItemByNameAjax(@RequestParam("itemName") String itemName) {
		 List<GameItem> items = itemService.findByLikeName(itemName);
		return items;
	}
	
	@GetMapping("/gameitem/gameitemUpdate")
	public String updatePage(@RequestParam("itemId") Integer itemId, Model m) {
		m.addAttribute("item", itemService.findById(itemId));
		m.addAttribute("games", itemService.findGameName());
		return "ni/itemUpdate";
	}
	
	@PutMapping("/gameitem/gameitemUpdate")
	public String update(@ModelAttribute GameItem item, @ModelAttribute Game game, @RequestParam("myImg") MultipartFile file) {
		try {
			if(file != null && !file.isEmpty()) {
				String fileName = file.getOriginalFilename();
				if(fileName != null && fileName.length() != 0) {
					item.setItemImgName(fileName);
					item.setItemImg(file.getBytes());
				}
			}
			item.setGame(game);
			itemService.updateById(item);
			return "redirect:/gameitem/allGameitem";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/gameitem";
		}
	}
	
	@GetMapping("/gameitem/newGameitem")
	public String insertPage(Model m) {
		m.addAttribute("games", itemService.findGameName());
		return "ni/itemInsert";
	}
	
	@PostMapping("/gameitem/newGameitem")
	public String insert(@ModelAttribute GameItem item, @ModelAttribute Game game, @RequestParam("myImg") MultipartFile file) {
		try {
			if(file != null && !file.isEmpty()) {
				item.setItemImgName(file.getOriginalFilename());
				item.setItemImg(file.getBytes());
			}
			itemService.insert(item);
			return "redirect:/gameitem/allGameitem";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/gameitem";
		}
	}
	
	@DeleteMapping("/gameitem/gameitemDelete2")
	public String delete(@RequestParam("itemId") Integer itemId) {
		itemService.delete(itemId);
		return "redirect:/gameitem/allGameitem";
	}
	
	@ResponseBody
	@DeleteMapping("/gameitem/gameitemDelete")
	public String deleteAjax(@RequestParam("itemId") Integer itemId) {
		itemService.delete(itemId);
		return "delete OK!";
	}
	
	@GetMapping("/gameitem/downloadImage/{itemId}")
	private ResponseEntity<byte[]> downloadImage(@PathVariable Integer itemId) {
		GameItem img1 = itemService.findById(itemId);
		byte[] itemImgFile = img1.getItemImg();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(itemImgFile, headers, HttpStatus.OK);
	}
	
	@GetMapping("/market/downloadImage/{itemId}")
	private ResponseEntity<byte[]> downloadMarketImage(@PathVariable Integer itemId) {
		GameItem img1 = itemService.findById(itemId);
		byte[] itemImgFile = img1.getItemImg();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_JPEG);
		return new ResponseEntity<byte[]>(itemImgFile, headers, HttpStatus.OK);
	}
	
	@ResponseBody
	@GetMapping("/gameitem/checkItemName")
	public boolean checkItemName(@RequestParam("itemName") String itemName, @RequestParam("gameId") Integer gameId) {
		return itemService.checkItemName(itemName, gameId);
	}
}
