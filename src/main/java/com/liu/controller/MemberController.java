package com.liu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired
	MemberService mService;

	@ResponseBody
	@GetMapping("/member/api/checkEmail")
	public String checkEmail(@RequestParam(name = "e") String email) {
		boolean result = mService.emailAlreadyRegistered(email);
		if(result == true) {
			return "isExist";
		}else {
			return "isNotExist";
		}
	}
}
