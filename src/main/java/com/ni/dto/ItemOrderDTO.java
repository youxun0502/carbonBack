package com.ni.dto;

import java.util.Date;

import com.liu.model.Member;
import com.ni.model.GameItem;

import lombok.Data;

@Data
public class ItemOrderDTO {

	private Integer ordId;
	private Integer itemId;
	private Integer buyer;
	private Integer seller;
	private Integer quantity;
	private Float price;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	
	private Member buy;
	private Member sell;
	private GameItem gameItem;
	
	private Float minPrice;
	private Float total;
	
	private Integer needFund;
	
	private Float getTotal() {
		total = (float) 0;
		total += price * quantity;
		return total;
	}
}
