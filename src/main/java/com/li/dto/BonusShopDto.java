package com.li.dto;


import lombok.Data;

@Data
public class BonusShopDto {	
		private Integer logId;
		
		private Integer BonusId;
		private Integer memberId;		
		private String bonusName;
		private Integer bonusprice;
		private String bonusDes;
		private Boolean status;
		private Integer point;
}
