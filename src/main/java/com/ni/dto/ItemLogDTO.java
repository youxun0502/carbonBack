package com.ni.dto;

import java.util.Date;

import com.liu.model.Member;
import com.ni.model.GameItem;
import com.ni.model.ItemOrder;

import lombok.Data;

@Data
public class ItemLogDTO {

	private Integer id;
	private Integer ordId;
	private Integer itemId;
	private Integer memberId;
	private Integer quantity;
	private Integer total;
	private Date createTime;
	
	private Member member;
	private GameItem gameItem;
	private ItemOrder itemOrder;
}
