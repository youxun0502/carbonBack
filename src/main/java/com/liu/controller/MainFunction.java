package com.liu.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.config.PreviousPage;
import com.liu.dto.MemberDto;
import com.liu.model.Member;
import com.liu.service.GmailService;
import com.liu.service.MemberService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainFunction {

	@Autowired
	private PreviousPage previousPage;

	@Autowired
	private MemberService mService;

	@Autowired
	private GmailService gService;

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
	public String loginPage(@CookieValue(value = "email", required = false) String cookieValue, Model m,
			HttpServletRequest request, HttpSession session) {
		// 有登入就不能進去登入頁面
		if (session.getAttribute("character") != null) {
			return "redirect:/";
		}
		String url = request.getHeader("Referer");
		if (url != null) {
			String subUrl = url.substring(28, url.length());
			String[] splitUrl = subUrl.split("\\?");
			String returnUrl = splitUrl[0];
			previousPage.setPreviousPage(returnUrl);
		}

		m.addAttribute("email", cookieValue);
		return "/liu/memberLogin";
	}

	@PostMapping("/main/memberLogin")
	public String memberLogin(@RequestParam("email") String email, @RequestParam("memberPwd") String memberPwd,
			@RequestParam(name = "rememberMe", required = false) String rememberMe, Model m, HttpSession session,
			HttpServletResponse response, HttpServletRequest request) {
		System.out.println(email);
		System.out.println(memberPwd);
		Member member = mService.isMember(email, memberPwd);
		
		if (member == null) {
			m.addAttribute("status", "帳號或密碼輸入錯誤");
			return "/liu/memberLoginError";
		} else if (member.getStatus() == 2) {
			m.addAttribute("status", "此帳戶已被凍結");
			return "/liu/memberLoginError";
		} else if (member.getStatus() == 3) {
			m.addAttribute("status", "此帳戶尚未通過驗證");
			return "/liu/memberLoginError";
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

					if (cookie != null && cookie.getValue() != email) {
						cookie.setMaxAge(0);
						Cookie newCookie = new Cookie("email", email);
						newCookie.setMaxAge(60 * 60 * 24);
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

				if (previousPage.getPreviousPage() == null
						|| previousPage.getPreviousPage().equals("/main/registerPage")
						|| previousPage.getPreviousPage().equals("/main/logout")
						|| previousPage.getPreviousPage().equals("/main/emailVerification")) {
					return "/liu/home";
				} else {
					return "redirect:" + previousPage.getPreviousPage();
				}

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
				if (previousPage.getPreviousPage() == null
						|| previousPage.getPreviousPage().equals("/main/registerPage")
						|| previousPage.getPreviousPage().equals("/main/memberLogin")) {
					return "/liu/home";
				} else {
					return "redirect:" + previousPage.getPreviousPage();
				}
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
	public String memberRegister(@ModelAttribute("memberDto") MemberDto memberDto, Model m)
			throws AddressException, MessagingException, IOException {
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
		
		LocalDateTime nowTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String nowStringTime = nowTime.format(formatter);
		String url = "http://localhost:8080/carbon/main/emailVerification?id="+member.getId()+"&t="+nowStringTime;
		gService.sendMessage(memberDto.getEmail(), gService.getMyEmail(), "Carbon邀請您驗證您的信箱", "此為系統發送郵件，請勿直接回覆！！！\n"
				+ "\n" + memberDto.getId() + "您好:\n" + "\n" + "點選以下連結驗證信箱\n" + "\n" +url+"\n\n"+"Carbon lys7744110@gmail.com");
		m.addAttribute("registration", "success");
		return "/liu/memberLogin";
	}

	@GetMapping("/main/emailVerification")
	public String emailVerification(@RequestParam(name = "id") Integer id, @RequestParam("t") String time, Model m ) {

		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime getVerificationTime = LocalDateTime.parse(time, formatter);
		LocalDateTime nowTime = LocalDateTime.now();

		Duration duration = Duration.between(getVerificationTime, nowTime);

		long minutes = duration.toMinutes();

		if (minutes < 5) {
			Member member = mService.findById(id);
			member.setStatus(1);
			boolean result = mService.updateStatus(member);
			
			if(result == true) {
				m.addAttribute("status", "信箱驗證成功");
				return "/liu/emailVerification";
			}else {
				m.addAttribute("status", "信箱驗證失敗，請聯繫客服");
				return "/liu/emailVerification";
			}
		}else {
			m.addAttribute("status", "驗證網址過期，請重新取得驗證網址");
			return "/liu/emailVerification";
		}

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
