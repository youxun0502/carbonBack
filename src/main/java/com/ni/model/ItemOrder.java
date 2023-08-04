package com.ni.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liu.model.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity @Table(name = "itemOrder")
public class ItemOrder {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ordId;
	private Integer itemId;
	private Integer buyer;
	private Integer seller;
	
	@ManyToOne
	@JoinColumn(name = "BUYER", insertable = false, updatable = false)
	private Member buy;
	
	@ManyToOne
	@JoinColumn(name = "SELLER", insertable = false, updatable = false)
	private Member sell;
	private Integer quantity;
	private Float price;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	
	@ManyToOne
	@JoinColumn(name = "ITEMID", insertable = false, updatable = false)
	private GameItem gameItem;
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "itemOrder", cascade = CascadeType.ALL)
	private ItemLog itemLog;
	
	@PrePersist
	public void onCreate() {
		if(createTime == null) {
			createTime = new Date();
		}
	}
	
	@PreUpdate
	public void onUpdate() {
		updateTime = new Date();
	}
}
