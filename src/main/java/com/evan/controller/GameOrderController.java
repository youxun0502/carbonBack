package com.evan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evan.service.GameOrderService;
import com.liu.model.Member;

import jakarta.servlet.http.HttpSession;

@RestController
public class GameOrderController {

	@Autowired
	private GameOrderService goService;
	
	@PostMapping("/gameFront/order/ecpayCheckout")
	public String ecpayCheckout(@RequestParam Map<String, Object> formData) {
		String aioCheckOutALLForm = goService.ecpayCheckout(formData);
		
		return aioCheckOutALLForm;
	}
	

	
}
