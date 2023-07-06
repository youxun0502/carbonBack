package com.evan.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class OrderDTO {
	
	private Integer OrderID;
	
	private Date createDate;
	
	private List<OrderLogDTO> logs;
	
	private Integer totalPrice;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status==1?"已付款":"未付款";
	}
	
	
}
