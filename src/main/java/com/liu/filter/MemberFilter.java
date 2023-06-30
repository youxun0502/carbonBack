//package com.liu.filter;
//
//import java.io.IOException;
//
//import jakarta.servlet.Filter;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpSession;
//
//@WebFilter(urlPatterns = {"/member/*","/bonus/*","/discussions/*","/game/*","/gameitem/*","/competition/*","/event/*"})
//public class MemberFilter implements Filter {
//
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
//			throws IOException, ServletException {
//
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		HttpSession session = httpRequest.getSession();
//
//		String character = (String) session.getAttribute("character");
//
//		if ("member".equals(character)) {
//			filterChain.doFilter(request, response);
//		} else {
//			request.getRequestDispatcher("/main/goBackToHome").forward(request, response);
//		}
//
//	}
//}
