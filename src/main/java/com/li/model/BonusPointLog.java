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
@Table(name = "bonuspointlog")
public class BonusPointLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "logId")
	private Integer logId;

	@Column(name = "memberId")
	private Integer memberId;
	@Column(name = "logtype")
	private String logtype;
	@Column(name = "prepoint")
	private Integer prepoint;
	@Column(name = "changepoint")
	private Integer changepoint;
	@Column(name = "point")
	private Integer point;
	@Column(name = "updateTime")
	private Date updateTime;
	
	@ManyToOne
	@JoinColumn(name = "MEMBERID", insertable = false, updatable = false)
	private Member member;
	
	@PrePersist
	public void onCreate() {
		if(updateTime == null) {
			updateTime = new Date();
		}
			
	}
}
