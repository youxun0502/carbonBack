package com.li.dto;

import java.util.Date;

import com.li.model.BonusItem;
import com.liu.model.Member;

import lombok.Data;

@Data
public class BonusPointDto {
	private Integer logId;
	private Integer memberId;
	private String logtype;
	private Integer changepoint;
	private Integer point;
	private Date updateTime;
	
	private Member member;
}
