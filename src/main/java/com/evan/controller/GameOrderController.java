package com.evan.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.evan.service.GameOrderService;



@RestController
public class GameOrderController {

	@Autowired
	private GameOrderService goService;


	//第一支呼叫綠界的controller
	@PostMapping("/gameFront/order/ecpayCheckout")
	public String ecpayCheckout(@RequestParam Map<String, Object> formData) {
		String aioCheckOutALLForm = goService.ecpayCheckout(formData);

		return aioCheckOutALLForm;
	}

	//第一支呼叫linepay的controller
	@PostMapping("/gameFront/order/makePayment")
	public RedirectView  makeLinePayment(@RequestParam Map<String, Object> formData) {
		return goService.linePayFirstRequest(formData);
    }
	



}
