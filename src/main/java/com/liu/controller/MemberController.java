package com.liu.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		if (result == true) {
			return "isExist";
		} else {
			return "isNotExist";
		}
	}

	@ResponseBody
	@GetMapping("/member/api/checkPhone")
	public String checkPhone(@RequestParam(name = "p") String phone) {
		boolean result = mService.phoneAlreadyRegistered(phone);
		if (result == true) {
			return "isExist";
		} else {
			return "isNotExist";
		}
	}

	@PostMapping("/member/register")
	public String memberRegister(@ModelAttribute("memberDto") MemberDto memberDto, Model m) {
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
		m.addAttribute("registration", "success");
		return "/liu/memberLogin";
	}

	@GetMapping("/member")
	public String memberMain() {
		return "/liu/memberDataTable";
	}

	@GetMapping("/member/allMember")
	public String findAllMember(Model m) {
		List<Member> members = mService.findAll();
		m.addAttribute("members", members);
		return "/liu/memberDataTable";
	}

	@ResponseBody
	@GetMapping("/member/api/getUpdateData")
	public MemberDto getUpdateData(@RequestParam(name = "id") String id) {
		Member updateData = mService.findById(Integer.parseInt(id));
		if (updateData != null) {
			MemberDto memberDto = new MemberDto();
			memberDto.setInnerId(updateData.getId().toString());
			memberDto.setBirthday(updateData.getBirthday());
			memberDto.setEmail(updateData.getEmail());
			memberDto.setGender(updateData.getGender());
			memberDto.setLevel(updateData.getLevelId());
			memberDto.setName(updateData.getMemberName());
			memberDto.setId(updateData.getUserId());
			memberDto.setPhone(updateData.getPhone());
			memberDto.setPwd(updateData.getMemberPwd());
			memberDto.setRegistration(updateData.getRegistrationDate());
			memberDto.setAccount(updateData.getAccount());
			return memberDto;
		} else {
			return null;
		}
	}

	@ResponseBody
	@PutMapping("/member/api/update")
	public String update(@RequestBody MemberDto memberDto) {
		Member updateMember = mService.findById(Integer.parseInt(memberDto.getInnerId()));

		if (updateMember != null) {
			updateMember.setUserId(memberDto.getId());
			updateMember.setMemberPwd(memberDto.getPwd());
			updateMember.setMemberName(memberDto.getName());
			updateMember.setBirthday(memberDto.getBirthday());
			updateMember.setGender(memberDto.getGender());
			updateMember.setPhone(memberDto.getPhone());
			updateMember.setAccount(memberDto.getAccount());
			updateMember.setRegistrationDate(memberDto.getRegistration());
			updateMember.setLevelId(memberDto.getLevel());
			boolean status = mService.update(updateMember);

			if (status == true) {
				return "true";
			} else {
				return "false";
			}

		} else {
			return "false";
		}

	}
}
