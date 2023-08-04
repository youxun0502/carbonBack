package com.liu.dto;

public class ChatDto {

	private Integer fromMember;
	
	private Integer toMember;
	
	private String Content;

	public Integer getFromMember() {
		return fromMember;
	}

	public void setFromMember(Integer fromMember) {
		this.fromMember = fromMember;
	}

	public Integer getToMember() {
		return toMember;
	}

	public void setToMember(Integer toMember) {
		this.toMember = toMember;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}
	
	
}
