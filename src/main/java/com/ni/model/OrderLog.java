package com.ni.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Entity @Table(name = "orderLog")
public class OrderLog {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer logId;
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
	private Long price;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	
//	@JsonManagedReference(value = "orderLogs")
	@ManyToOne
	@JoinColumn(name = "ITEMID", insertable = false, updatable = false)
	private GameItem gameItem;
	@JsonIgnore
//	@JsonBackReference
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "orderLog", cascade = CascadeType.ALL)
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
