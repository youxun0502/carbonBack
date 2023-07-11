package com.evan.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionDeal {

	@ExceptionHandler(IndexOutOfBoundsException.class)
	public String indexException() {
		return "evan/404";
	}

	
}
