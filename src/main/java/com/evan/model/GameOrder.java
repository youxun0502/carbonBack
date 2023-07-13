package com.evan.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.liu.model.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity@Table(name="gameOrder")
public class GameOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer OrderId;
	
	@Column(name = "Id",insertable=false, updatable=false)
	private Integer Id;
	
	private Date createTime;
	
	private Integer status;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Id")
	private Member member;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gameOrder", cascade = CascadeType.ALL)
	private List<GameOrderLog> gameOrderLog;
	
	// 物件轉換成 Persistent 狀態以前做以下事情
	@PrePersist
	public void onCreate() {
		if(createTime == null ) {
			createTime = new Date();
		}
	}

}
