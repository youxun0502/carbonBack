package com.li.model;

import java.util.Date;

import com.liu.model.Member;

import jakarta.persistence.Column;
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
@Entity
@Table(name = "bonuslog")
public class BonusLog {

	@Id@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "logId")
	private Integer logId;
	@Column(name = "memberId")
	private Integer memberId;
	@Column(name = "bonusId")
	private Integer bonusId;
	@Column(name = "buyDate")
	private Date buyDate;
	
	@ManyToOne
	@JoinColumn(name = "BONUSID",insertable = false, updatable = false)
	private BonusItem bonusitem;
	@ManyToOne
	@JoinColumn(name = "MEMBERID",insertable = false, updatable = false)
	private Member member;
	
	@PrePersist
	public void onCreate() {
		if(buyDate == null) {
			buyDate = new Date();
		}
			
	}
	
	
}
