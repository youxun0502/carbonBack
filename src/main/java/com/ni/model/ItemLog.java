package com.ni.model;

import java.util.Date;

import com.liu.model.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity @Table(name = "itemLog")
public class ItemLog {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private Integer ordId;
	private Integer itemId;
	private Integer memberId;
	private Integer quantity;
	private Integer total;
	private Date createTime;
	
	@ManyToOne
	@JoinColumn(name = "MEMBERID", insertable = false, updatable = false)
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "ITEMID", insertable = false, updatable = false)
	private GameItem gameItem;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDID", insertable = false, updatable = false)
	private ItemOrder itemOrder;

	@PrePersist
	public void onCreate() {
		if(createTime == null) {
			System.out.println("create time");
			createTime = new Date();
		}
			
	}
}
