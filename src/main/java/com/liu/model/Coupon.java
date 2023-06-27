package com.liu.model;


import com.evan.model.GameType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "coupon")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "couponId")
	private Integer couponId;
	
	@Column(name = "typeId", insertable = false, updatable = false)
	private Integer typeId;
	
	@Column(name = "coupon")
	private Float coupon;
	
	@Column(name = "[desc]")
	private String desc;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	private GameType gameType;
	
	
	public Coupon() {}

}
