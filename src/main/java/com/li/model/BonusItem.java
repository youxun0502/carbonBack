package com.li.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "bonusitem")
public class BonusItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bonusId")
	private Integer bonusId;
	
	@Column(name = "bonusName")
	private String bonusName;
	
	@Column(name = "bonusDes")
	private String bonusDes;
	
	@Column(name = "bonusPrice")
	private Integer bonusPrice;
	
	@Column(name = "bonusImg")
	private String bonusImg;
	
	@Lob
	@JsonIgnore
	@Column(name = "img_file")
	private byte[] img_file;
	
	@Column(name = "status")
	private boolean status;
	
	@Column(name = "bonusType")
	private String bonusType;


	public BonusItem() {
	}

	public Integer getBonusId() {
		return bonusId;
	}

	public void setBonusId(Integer bonusId) {
		this.bonusId = bonusId;
	}

	public String getBonusName() {
		return bonusName;
	}

	public void setBonusName(String bonusName) {
		this.bonusName = bonusName;
	}

	public String getBonusDes() {
		return bonusDes;
	}

	public void setBonusDes(String bonusDes) {
		this.bonusDes = bonusDes;
	}

	public Integer getBonusPrice() {
		return bonusPrice;
	}

	public void setBonusPrice(Integer bonusPrice) {
		this.bonusPrice = bonusPrice;
	}

	public String getBonusImg() {
		return bonusImg;
	}

	public void setBonusImg(String bonusImg) {
		this.bonusImg = bonusImg;
	}

	

	public byte[] getImg_file() {
		return img_file;
	}

	public void setImg_file(byte[] img_file) {
		this.img_file = img_file;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getBonusType() {
		return bonusType;
	}

	public void setBonusType(String bonusType) {
		this.bonusType = bonusType;
	}
	
	
	
	
	
}
