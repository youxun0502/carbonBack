package com.liu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.dto.MemberDto;
import com.liu.model.Member;
import com.liu.service.MemberService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainFunction {

	@Autowired
	private MemberService mService;

	@GetMapping("/main/goBackToMain")
	public String goBackToMain() {
		return "liu/main";
	}
	@GetMapping("/main/goBackToHome")
	public String goBackToHome() {
		return "liu/home";
	}
	@GetMapping("/")
	public String homePage() {
		return "liu/home";
	}

	@GetMapping("/main/loginPage")
	public String loginPage(@CookieValue(value = "email", required = false) String cookieValue, Model m) {
		m.addAttribute("email", cookieValue);
		return "/liu/memberLogin";
	}

	@PostMapping("/main/memberLogin")
	public String memberLogin(@RequestParam("email") String email, @RequestParam("memberPwd") String memberPwd,
			@RequestParam(name = "rememberMe", required = false) String rememberMe, Model m, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {


		Member member = mService.isMember(email, memberPwd);
		if (member == null) {
			return "/liu/memberLoginError";
		} else if (member.getStatus() == 2) {
			return "/liu/memberCanNotLogin";
		} else if (member.getLevelId() == 100) {
			session.setAttribute("managerBeans", member);
			session.setAttribute("character", "manager");
			return "/liu/main";
		} else {		
			if ("1".equals(rememberMe)) { // 有rememberMe
				Cookie[] cookies = request.getCookies();
				Cookie cookie = null;
				
				if (cookies != null) {
					for (Cookie cookie1 : cookies) {
						if (cookie1.getName().equals("email")) {
							cookie = cookie1;

						}
					}

					if (cookie!=null &&cookie.getValue() != email) {
						cookie.setMaxAge(0);
						Cookie newCookie = new Cookie("email",email);
						newCookie.setMaxAge(60*60*24);
						newCookie.setHttpOnly(true);
						response.addCookie(cookie);
						response.addCookie(newCookie);
					}

				}
				if (cookie == null) {
					cookie = new Cookie("email", email);
					cookie.setMaxAge(60 * 60 * 24);
					cookie.setHttpOnly(true);
					response.addCookie(cookie);
					System.out.println("沒有cookie");
				}
				session.setAttribute("memberBeans", member);
				session.setAttribute("character", "member");
				return "/liu/home";

			} else { // 沒有rememberMe 就刪掉cookie
				Cookie[] cookies = request.getCookies();
				Cookie cookie = null;
				if (cookies != null) {
					for (Cookie cookie1 : cookies) {
						if (cookie1.getName().equals("email")) {
							cookie = cookie1;
						}
					}
				}

				if (cookie != null) {
					cookie.setMaxAge(0); // 清除cookie
					response.addCookie(cookie);// 清除cookie
				}
				session.setAttribute("memberBeans", member);
				session.setAttribute("character", "member");
				return "/liu/home";
			}

		}
	}

	@GetMapping("/main/registerPage")
	public String memberRegister(Model m) {
		m.addAttribute("memberDto", new MemberDto());
		return "/liu/memberRegister";
	}
	@GetMapping("/main/logout")
	public String memberLogout(@RequestParam(name = "id") Integer id, HttpSession session, Model m) {
		session.invalidate();
		return "/liu/home";
	}
	@PostMapping("/main/register")
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
	@ResponseBody
	@GetMapping("/main/api/checkEmail")
	public String checkEmail(@RequestParam(name = "e") String email) {
		boolean result = mService.emailAlreadyRegistered(email);
		if (result == true) {
			return "isExist";
		} else {
			return "isNotExist";
		}
	}

	@ResponseBody
	@GetMapping("/main/api/checkPhone")
	public String checkPhone(@RequestParam(name = "p") String phone) {
		boolean result = mService.phoneAlreadyRegistered(phone);
		if (result == true) {
			return "isExist";
		} else {
			return "isNotExist";
		}
	}

}
