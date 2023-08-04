package com.ni.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liu.model.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity @Table(name = "wallet")
public class Wallet {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; 
	private Integer memberId;
	private String tradeNo;
	private Float change;
	private Float balance;
	private String changeDesc;
	private Date createTime;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "MEMBERID", insertable = false, updatable = false)
	private Member member;
	
	@PrePersist
	public void onCreate() {
		if(createTime == null) {
			createTime = new Date();
		}
	}
}
