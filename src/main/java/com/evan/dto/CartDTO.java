package com.evan.dto;

import lombok.Data;

@Data
public class CartDTO {

	private Integer memberId;
	
	private Integer photoId;
	
	private Integer price;
	
	private String gameName;
	
	private Integer gameId;
}
