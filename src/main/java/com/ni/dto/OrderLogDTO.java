package com.ni.dto;

import lombok.Data;

@Data
public class OrderLogDTO {

	private Integer logId;
	private Integer itemId;
	private String itemName;
	private String itemImgName;
	private Integer buyer;
	private Integer seller;
	private Integer amount;
	private Long price;
	private Integer status;
	private Integer gameId;
	private String gameName;
	private String userId;
}
