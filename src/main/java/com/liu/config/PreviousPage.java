package com.liu.config;

import org.springframework.stereotype.Component;

@Component
public class PreviousPage {

	private String previousPage;

	public String getPreviousPage() {
		return previousPage;
	}

	public void setPreviousPage(String previousPage) {
		this.previousPage = previousPage;
	}
}
