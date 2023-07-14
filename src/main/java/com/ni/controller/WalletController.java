package com.ni.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.model.Member;
import com.liu.service.GmailService;
import com.liu.service.MemberService;
import com.ni.dto.WalletDTO;
import com.ni.service.WalletService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

@Controller
public class WalletController {

	@Autowired
	private WalletService walletService;
	
	@Autowired
	private GmailService gService;
	
	@Autowired
	private MemberService mService;
	
	@GetMapping("/profile/wallet")
	public String findByMemberId() {
		return "ni/myWallet";
	}
	
	@ResponseBody
	@GetMapping("/profile/myWallet")
	public WalletDTO findBalance(@RequestParam("memberId") Integer memberId) {
		return walletService.findBalance(memberId);
	}
	
	@ResponseBody
	@PostMapping("/profile/wallet/addFunds")
	public String addFund(@RequestParam Map<String, Object> wallet) 
			throws AddressException, MessagingException, IOException {
		String aioCheckOutALLForm = walletService.ecpayCheckout(wallet);
		return aioCheckOutALLForm;
	}
	
	@GetMapping("/profile/wallet/status")
	public String checkStatus(@RequestParam Map<String, Object> walletForm, Model m) 
			throws AddressException, MessagingException, IOException {
		walletService.postQueryTradeInfo(walletForm);
		Member member = mService.findById(Integer.parseInt((String)walletForm.get("memberId")));
		
		String url = "http://localhost:8080/carbon/profile/"+ member.getId() +"/wallet";
		
		gService.sendMessage(member.getEmail(), gService.getMyEmail(), "Carbon錢包儲值成功",
				"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + member.getUserId() + "您好:\n" + "\n" + 
				"感謝您此次於Carbon完成儲值，點選以下連結前往個人頁面\n" + "\n" + url
						+ "\n\n" + "Carbon lys7744110@gmail.com");
		return "redirect:/market";
	}
	
	
}
