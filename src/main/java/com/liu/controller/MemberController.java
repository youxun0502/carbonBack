package com.liu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.dto.MemberDto;
import com.liu.model.Member;
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
	
	@ResponseBody
	@GetMapping("/member/api/checkPhone")
	public String checkPhone(@RequestParam(name = "p") String phone) {
		boolean result = mService.phoneAlreadyRegistered(phone);
		if(result == true) {
			return "isExist";
		}else {
			return "isNotExist";
		}
	}
	
	@PostMapping("/member/register")
	public String memberRegister(@ModelAttribute("memberDto") MemberDto memberDto ,Model m) {
		Member member = new Member();
		member.setUserId(memberDto.getId());
		member.setEmail(memberDto.getEmail());
		member.setMemberPwd(memberDto.getPwd());
		member.setMemberName(memberDto.getName());
		member.setBirthday(memberDto.getBirthday());
		member.setGender(memberDto.getGender());
		member.setPhone(memberDto.getPhone());
		member.setAccount(null);
		mService.insert(member);
		m.addAttribute("registration","success");
		return "/liu/memberLogin";
	}
}
