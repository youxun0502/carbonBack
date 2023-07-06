package com.evan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evan.service.GameOrderService;

@RestController
public class GameOrderController {

	@Autowired
	private GameOrderService goService;
	
	@PostMapping("/ecpayCheckout")
	public String ecpayCheckout() {
		String aioCheckOutALLForm = goService.ecpayCheckout();
		
		return aioCheckOutALLForm;
	}
	
}
