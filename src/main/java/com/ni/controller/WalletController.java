package com.ni.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ni.service.WalletService;

@Controller
public class WalletController {

	@Autowired
	private WalletService walletService;
	
	@GetMapping("/account/{id}/wallet")
	public void findByMemberId() {
	}
	
	@ResponseBody
	@PostMapping("/profiles/wallet/addFunds")
	public String addFund(@RequestParam Map<String, Object> wallet) {
		String aioCheckOutALLForm = walletService.ecpayCheckout(wallet);
		return aioCheckOutALLForm;
	}
	
	@GetMapping("/profiles/wallet/status")
	public String checkStatus(@RequestParam Map<String, Object> walletForm, Model m) {
		walletService.postQueryTradeInfo(walletForm);
		return "redirect:/market";
	}
	
	
}
