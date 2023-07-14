package com.li.dto;


import java.util.Date;

import com.li.model.BonusItem;
import com.liu.model.Member;

import lombok.Data;

@Data
public class BonusShopDto {	
		private Integer logId;
		
		private Integer bonusId;
		private Integer memberId;		
		private String bonusName;
		private String bonusType;
		private Integer bonusprice;
		
		
		private String bonusDes;
		private Boolean status;
		private Boolean isBought;
		private Integer point;
		private Date buyDate;
		
		private Member member;
		private BonusItem bonusitem;
}
