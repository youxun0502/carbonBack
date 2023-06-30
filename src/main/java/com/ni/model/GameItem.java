package com.ni.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.evan.model.Game;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity @Table(name = "gameItem")
public class GameItem {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemId;
	private String itemName;
	private Integer gameId;
	private String itemDesc;
	private String itemImgName;
	 
	@Lob @JsonIgnore
	private byte[] itemImg;
	private String itemGrade;
	private String itemType;
	private Integer status;
	private Date createTime;
	private Date updateTime;
	
	@ManyToOne
	@JoinColumn(name = "GAMEID", insertable = false, updatable = false)
	private Game game;
	@JsonIgnore
//	@JsonBackReference(value = "itemOrders")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gameItem", cascade = CascadeType.ALL)
	private List<ItemOrder> itemOrders = new ArrayList<>();
	@JsonIgnore
//	@JsonBackReference(value = "itemLogs")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gameItem", cascade = CascadeType.ALL)
	private List<ItemLog> itemLogs = new ArrayList<>();

	@PrePersist
	public void onCreateAndUpdate() {
		System.out.println("new createTime");
		if(createTime == null) {
			createTime = new Date();
		} 
	}
	
	@PreUpdate
	public void onUpdate() {
		System.out.println("new updateTime");
		updateTime = new Date();
	}
	
	public GameItem() {
	}

	public GameItem(String itemName, int gameId, String itemDesc, String itemImgName, byte[] itemImg, String itemGrade, String itemType,
			int status, Date createTime) {
		this.itemName = itemName;
		this.gameId = gameId;
		this.itemDesc = itemDesc;
		this.itemImgName = itemImgName;
		this.itemImg = itemImg;
		this.itemGrade = itemGrade;
		this.itemType = itemType;
		this.status = status;
		this.createTime = createTime;
	}
	
	public GameItem(int itemId, String itemName, int gameId, String itemDesc, String itemImgName, byte[] itemImg,
			String itemGrade, String itemType, int status, Date updateTime, Game game) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.gameId = gameId;
		this.itemDesc = itemDesc;
		this.itemImgName = itemImgName;
		this.itemImg = itemImg;
		this.itemGrade = itemGrade;
		this.itemType = itemType;
		this.status = status;
		this.updateTime = updateTime;
		this.game = game;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("GameItem [itemId=");
		builder.append(itemId);
		builder.append(", itemName=");
		builder.append(itemName);
		builder.append(", gameId=");
		builder.append(gameId);
		builder.append(", itemDesc=");
		builder.append(itemDesc);
		builder.append(", itemImgName=");
		builder.append(itemImgName);
		builder.append(", itemImg=");
		builder.append(Arrays.toString(itemImg));
		builder.append(", itemGrade=");
		builder.append(itemGrade);
		builder.append(", itemType=");
		builder.append(itemType);
		builder.append(", status=");
		builder.append(status);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", updateTime=");
		builder.append(updateTime);
		builder.append(", game=");
		builder.append(game);
		builder.append("]");
		return builder.toString();
	}
}
