package com.ni.dto;

import java.util.Date;

import com.liu.model.Member;

import lombok.Data;

@Data
public class WalletDTO {

	private Integer id; 
	private Integer memberId;
	private String tradeNo;
	private Float change;
	private Float balance;
	private String changeDesc;
	private Date createTime;
	
	private Member member;
	
	private String addAmount;
}
