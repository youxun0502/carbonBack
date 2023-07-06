package com.evan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evan.service.GameOrderService;
import com.ni.service.OrderService;

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
