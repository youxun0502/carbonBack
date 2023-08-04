package com.evan.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "gameOrderLog")
public class GameOrderLog {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderLogId;
	
	@Column(name = "orderId",insertable=false, updatable=false)
    private Integer orderId;

    private String gameName;

    private Integer priceLog;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private GameOrder gameOrder;
}