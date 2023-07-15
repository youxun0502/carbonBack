package com.liu.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.dto.OrderLogDTO;
import com.evan.service.GameOrderService;
import com.li.model.BonusItem;
import com.li.service.BonusService;
import com.liu.dto.MemberDto;
import com.liu.model.Member;
import com.liu.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	@Autowired
	MemberService mService;
	
	@Autowired
	GameOrderService gameOrderService;
	
	@Autowired
	BonusService bService;

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
			System.out.println("DTO:" + updateData.getMemberPwd());
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
		boolean status = mService.update(memberDto);

		if (status == true) {
			return "true";
		} else {
			return "false";
		}
	}

	@ResponseBody
	@PutMapping("/member/api/delete")
	public String delete(@RequestBody Map<String, Integer> deleteId) {
		boolean status = mService.deleteById(deleteId.get("id"));
		if (status == true) {
			return "finish";
		} else {
			return "fail";
		}

	}

	@ResponseBody
	@PutMapping("/member/api/restore")
	public String restore(@RequestBody Map<String, Integer> restoreId) {
		boolean status = mService.restoreById(restoreId.get("id"));
		if (status == true) {
			return "finish";
		} else {
			return "fail";
		}
	}

	@ResponseBody
	@GetMapping("/member/api/seachByName")
	public List<MemberDto> findMemberByName(@RequestParam(name = "name") String name) {
		List<MemberDto> memberDtos = new ArrayList<>();
		List<Member> members = mService.findByName(name);
		for (Member member : members) {
			MemberDto memberDto = new MemberDto();
			memberDto.setInnerId(member.getId().toString());
			memberDto.setBirthday(member.getBirthday());
			memberDto.setEmail(member.getEmail());
			memberDto.setGender(member.getGender());
			memberDto.setLevel(member.getLevelId());
			memberDto.setName(member.getMemberName());
			memberDto.setId(member.getUserId());
			memberDto.setPhone(member.getPhone());
			memberDto.setPwd(member.getMemberPwd());
			memberDto.setRegistration(member.getRegistrationDate());
			memberDto.setAccount(member.getAccount());
			memberDto.setStatus(member.getStatus());
			memberDtos.add(memberDto);
		}
		return memberDtos;
	}

	@GetMapping("/member/memberRegistrationDateAnalysisPage")
	public String memberRegistrationDateAnalysisPage(Model m, @RequestParam(name = "year", defaultValue = "2023") String  year) {
		List<Object[]> datas = mService.findRegistrationMonth(year);
		List<String> years = mService.findAllRegistrationYear();
		List<Object[]> genderData = mService.findRegistrationGender(year);
		m.addAttribute("datas", datas);
		m.addAttribute("year", year);
		m.addAttribute("years", years);
		System.out.println(genderData.get(0)[1]);
		m.addAttribute("genderData", genderData);
		return "/liu/memberRegistrationDateAnalysisPage";
	}

	@GetMapping("/member/api/memberRegistrationDateAnalysis")
	@ResponseBody
	public List<Object[]> memberRegistrationDateAnalysis(@RequestParam(name = "year", defaultValue = "2023") String  year) {
		return mService.findRegistrationMonth(year);
	}
	
	@GetMapping("/member/api/memberRegistrationgGenderAnalysis")
	@ResponseBody
	public List<Object[]> memberRegistrationGenderAnalysis(@RequestParam(name = "year", defaultValue = "2023") String  year) {
		return mService.findRegistrationGender(year);
	}
	
	@GetMapping("/memberFront/memberInformationPage")
	public String memberInformationPage(HttpSession session, Model m) {
		Member member = (Member)session.getAttribute("memberBeans");
		Map<String, Object> formData = new HashMap<>();
		formData.put("memberId",member.getId().toString());
		List<OrderLogDTO> memberOwnGames = gameOrderService.getMemberOwnGames(formData);
		List<BonusItem> list = bService.findAll();
		session.setAttribute("memberBeans", mService.findById(member.getId()));
		m.addAttribute("bonusitemList", list);
		m.addAttribute("memberOwnGames", memberOwnGames);
		return "/liu/memberInformationPage";
	}
}
