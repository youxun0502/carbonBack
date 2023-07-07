package com.ni.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ni.service.ItemEcpayService;


@RestController
public class ItemEcpayController {

	@Autowired
	ItemEcpayService itemEcpayService;
	
	@PostMapping("/market/ecpayCheckout")
	public String ecpayCheckout() {
		String aioCheckOutALLForm = itemEcpayService.ecpayCheckout();
		
		return aioCheckOutALLForm;
	}
		
}
