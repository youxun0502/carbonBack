package com.li.dto;


import lombok.Data;

@Data
public class BonusShopDto {
	

		private Integer logId;
		private Integer BonusId;
		private Integer userId;		
		private String bonusName;
		private Integer bonusprice;
		private Boolean status;
	    private String bonusDes;
}
