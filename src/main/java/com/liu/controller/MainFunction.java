package com.liu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainFunction {

	@GetMapping("/")
	public String goBackTOHome() {
		return "liu/main";
	}
}
