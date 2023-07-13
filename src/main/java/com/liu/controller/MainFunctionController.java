package com.liu.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chen.model.Competition;
import com.chen.model.CompetitionRepository;
import com.chen.model.Event;
import com.chen.model.EventRepository;
import com.li.service.BonusLogService;
import com.li.service.BonusPointService;
import com.evan.service.GameService;
import com.evan.utils.SortChartJs;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import com.liu.config.PreviousPage;
import com.liu.dto.MemberDto;
import com.liu.model.Member;
import com.liu.service.GLoginService;
import com.liu.service.GmailService;
import com.liu.service.MemberService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainFunctionController {

	@Autowired
	private PreviousPage previousPage;

	@Autowired
	private MemberService mService;

	@Autowired
	private GmailService gService;

	@Autowired
	private BonusPointService bpService;
	
	@Autowired
	private BonusLogService blService;

	@Autowired
	private SortChartJs sortChartJs;

	@Autowired
	private GameService gameService;

	@Autowired
	private CompetitionRepository cRepo;

	@Autowired
	private EventRepository eRepo;

	@Autowired
	private GLoginService gLoginService;

	@GetMapping("/main/goBackToMain")
	public String goBackToMain() {
		return "liu/main";
	}

	@GetMapping("/main/goBackToHome")
	public String goBackToHome() {
		return "redirect:/";
	}

	@GetMapping("/")
	public String homePage(Model model) {
		sortChartJs.sortGameDTOAll(gameService.getAllGameInfo());
		model.addAttribute("gameList", sortChartJs.getGameList());
		List<Competition> competitionList = cRepo.findAllOrderByStartDate();
		model.addAttribute("competitionList", competitionList);
		List<Event> eventList = eRepo.findAllOrderByStartDate();
		model.addAttribute("eventList", eventList);

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
					for (Cookie cookie1 : cookies) { //看現有的cookie有沒有重複叫做email的
						if (cookie1.getName().equals("email")) {
							cookie = cookie1;

						}
					}

					if (cookie != null && cookie.getValue() != email) {
						cookie.setMaxAge(0);	//清除舊的cookie(1)
						Cookie newCookie = new Cookie("email", email);
						newCookie.setMaxAge(60 * 60 * 24);
						newCookie.setHttpOnly(true);
						response.addCookie(cookie); //清除舊的cookie(2)
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
				return memberLogin(session, member);

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
				return memberLogin(session, member);
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
		return "redirect:/";
	}

	@ResponseBody
	@PostMapping("/main/api/register")
	public String memberRegister(@RequestBody MemberDto memberDto)
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
		member.setUseAvatar(1);
		member.setUseFrame(2);
		member.setUseBackground(3);
		mService.insert(member);
		blService.initAccountAvatar( mService.findByEmail(memberDto.getEmail()).getId());
		bpService.newPointLog("register", mService.findByEmail(memberDto.getEmail()).getId(), 2000);
		LocalDateTime nowTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String nowStringTime = nowTime.format(formatter);
		String url = "http://localhost:8080/carbon/main/emailVerification?c=12edf@af" + "&t=" + nowStringTime
				+ "&userId=" + member.getId();
		gService.sendMessage(memberDto.getEmail(), gService.getMyEmail(), "Carbon邀請您驗證您的信箱",
				"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + memberDto.getId() + "您好:\n" + "\n" + "點選以下連結驗證信箱\n" + "\n" + url
						+ "\n\n" + "Carbon lys7744110@gmail.com");
		return "success";
	}

	@GetMapping("/main/emailVerification")
	public String emailVerification(@RequestParam(name = "userId") Integer id, @RequestParam("t") String time,
			Model m) {

		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime getVerificationTime = LocalDateTime.parse(time, formatter);
		LocalDateTime nowTime = LocalDateTime.now();

		Duration duration = Duration.between(getVerificationTime, nowTime);

		long minutes = duration.toMinutes();

		if (minutes < 5) {
			Member member = mService.findById(id);
			member.setStatus(1);
			boolean result = mService.updateStatus(member);

			if (result == true) {
				m.addAttribute("status", "信箱驗證成功");
				return "/liu/memberEmailVerification";
			} else {
				m.addAttribute("status", "信箱驗證失敗，請聯繫客服");
				return "/liu/memberEmailVerification";
			}
		} else {
			m.addAttribute("status", "驗證網址過期，請重新取得驗證網址");
			return "/liu/memberEmailVerification";
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

	@GetMapping("/main/api/getEmail")
	@ResponseBody
	public String getVerifyEmail(@RequestParam("id") Integer id)
			throws AddressException, MessagingException, IOException {
		System.out.println(id);
		Member member = mService.findById(id);
		String email = member.getEmail();
		String userId = member.getUserId();
		LocalDateTime nowTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
		String nowStringTime = nowTime.format(formatter);
		String url = "http://localhost:8080/carbon/main/emailVerification?c=12edf@af" + "&t=" + nowStringTime
				+ "&userId=" + member.getId();
		gService.sendMessage(email, gService.getMyEmail(), "Carbon邀請您驗證您的信箱", "此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + userId
				+ "您好:\n" + "\n" + "點選以下連結驗證信箱\n" + "\n" + url + "\n\n" + "Carbon lys7744110@gmail.com");
		return "success";
	}

	@GetMapping("/main/api/forgetPwd")
	@ResponseBody
	public String forgetPassword(@RequestParam("email") String email)
			throws AddressException, MessagingException, IOException {
		String emailForFinding = email;

		boolean status = mService.emailAlreadyRegistered(emailForFinding);

		if (status == true) {
			LocalDateTime nowTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
			String nowStringTime = nowTime.format(formatter);
			String url = "http://localhost:8080/carbon/main/forgetPwdPage?email=" + emailForFinding + "&t="
					+ nowStringTime;
			gService.sendMessage(email, gService.getMyEmail(), "【Carbon】帳戶救援", "此為系統發送郵件，請勿直接回覆！！！\n" + "\n"
					+ "點選以下連結進行帳號救援\n" + "\n" + url + "\n\n" + "Carbon lys7744110@gmail.com");

		}
		return "success";
	}

	@GetMapping("/main/forgetPwdPage")
	public String forgetPwdPage(@RequestParam("email") String email, @RequestParam("t") String time, Model m) {
		LocalDateTime nowTime = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;
		LocalDateTime oldTime = LocalDateTime.parse(time, dateTimeFormatter);

		Duration duration = Duration.between(oldTime, nowTime);

		long minutes = duration.toMinutes();

		if (minutes <= 5) {
			m.addAttribute("email", email);
			m.addAttribute("isExpiration", "no");
			return "/liu/memberForgetPwdPage";
		} else {
			m.addAttribute("isExpiration", "yes");
			return "/liu/memberForgetPwdPage";
		}

	}

	@PutMapping("/main/updatePwdForForgetPwd")
	public String updatePwdForForgetPwd(@RequestParam("email") String email, @RequestParam("newPwd") String pwd) {
		mService.changePwdForForgetPwd(email, pwd);
		return "redirect:/main/loginPage";
	}

	@PostMapping("/main/googleLogin")
	public String googleLoginAndRegistration(@RequestParam("credential") String credential, Model m,
			HttpSession session) throws GeneralSecurityException, IOException {

		GoogleIdToken idToken = gLoginService.getIdToken(credential);
		Map<String, String> userInfo;
		if (idToken != null) {
			userInfo = gLoginService.getUserInfo(idToken);
			String email = "G" + userInfo.get("email");
			boolean emailAlreadyRegistered = mService.emailAlreadyRegistered(email);
			if (!emailAlreadyRegistered) {
				System.out.println("沒有帳號");
				// 沒註冊就註冊後登入
				Member member = new Member();
				member.setUserId(userInfo.get("name"));
				member.setEmail(email);
				member.setMemberPwd(userInfo.get("userId"));
				member.setMemberName(userInfo.get("name"));
				member.setBirthday(new Date());
				member.setGender(3);
				member.setPhone(null);
				member.setAccount(null);
				member.setUseAvatar(1);
				member.setUseFrame(2);
				member.setUseBackground(3);
				mService.insert(member);
				blService.initAccountAvatar( mService.findByEmail(email).getId());
				bpService.newPointLog("register", mService.findByEmail(email).getId(), 2000);
			}
			// 登入
			Member member = mService.isMember(email, userInfo.get("userId"));
			if (member.getStatus() == 2) {
				m.addAttribute("status", "此帳戶已被凍結");
				return "/liu/memberLoginError";
			} else {
				return memberLogin(session,member);
			}

		}
		return "redirect:/";
	}
	
	/*方法:會員登入*/
	private String memberLogin(HttpSession session, Member member) {
		session.setAttribute("memberBeans", member);
		session.setAttribute("character", "member");

		if (previousPage.getPreviousPage() == null
				|| previousPage.getPreviousPage().equals("/main/registerPage")
				|| previousPage.getPreviousPage().equals("/main/logout")
				|| previousPage.getPreviousPage().equals("/main/emailVerification")
				|| previousPage.getPreviousPage().equals("/main/memberLogin")
				|| previousPage.getPreviousPage().equals("/main/forgetPwdPage")) {
			return "redirect:/";
		} else {
			return "redirect:" + previousPage.getPreviousPage();
		}
	}

}
