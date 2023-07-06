package com.evan.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.dto.CartDTO;
import com.evan.dto.TypeDTO;
import com.evan.service.CartService;
import com.evan.service.GameOrderService;
import com.liu.model.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	private CartService cService;
	
	@Autowired
	private GameOrderService goService;

	@ResponseBody
	@GetMapping("/gameCart")
	public List<CartDTO> getCartByMemberId(@RequestParam Map<String, Object> formData) {
		int memberId = Integer.parseInt((String)(formData.get("memberId")));
		System.out.println(memberId);
		return cService.getMemberCart(memberId);
	}
	
	@ResponseBody
	@PostMapping("/gameCart/add")
	public List<CartDTO> addCartByMemberId(@RequestParam Map<String, Object> formData) {
		cService.addMemberCart(formData);
		System.out.println(formData);
		return cService.addMemberCart(formData);
	}
	
	@ResponseBody
	@PostMapping("/gameCart/addOne")
	public List<CartDTO> addOneCartByMemberId(@RequestParam Map<String, Object> formData) {
		cService.addOne(formData);
		System.out.println(formData);
		return cService.addMemberCart(formData);
	}
	
	@ResponseBody
	@DeleteMapping("/gameCart/delete")
	public List<CartDTO> deleteOneCartByMemberId(@RequestParam Map<String, Object> formData) {
		cService.deleteOne(formData);
		System.out.println(formData);
		return cService.addMemberCart(formData);
	}
	
	@GetMapping("/gameFront/cartList")
	public String cartList(@RequestParam("memberId") String memberId,Model model) {
		List<CartDTO> memberCart = cService.getMemberCart(Integer.parseInt(memberId));
		
		int count = 0 ;
		for (CartDTO cartDTO : memberCart) { count = count + cartDTO.getPrice();}
		
		model.addAttribute("cartList",memberCart);
		model.addAttribute("countSum",count);
		return "evan/cartList";
	}
	
	
	@PostMapping("/gameFront/orderList")
	public String addOrderList(@RequestParam Map<String, Object> formData,Model model) {
		
		goService.addOrder(formData);
		cService.deleteAll(formData);
		
		model.addAttribute("orderList",goService.getOrders(formData));
		return "evan/orderList";
	}
	
	@DeleteMapping("/gameFront/orderList/delete")
	public String deleteOrderList(@RequestParam Map<String, Object> formData,Model model) {
		
		goService.deleteOrder(formData);
		model.addAttribute("orderList",goService.getOrders(formData));
		return "evan/orderList";
	}

	@GetMapping("/gameFront/order/ecpaystatus")
	public String checkTradingStatus(@RequestParam Map<String, Object> formData,HttpSession session,Model model) {
		goService.ecpayTradingStatus(formData);
		goService.getOrders(formData);
		
		model.addAttribute("orderList",goService.getOrders(formData));
		
		return "evan/orderList";
	}
	
	
}
