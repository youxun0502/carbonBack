package com.liu.model;



import java.util.Set;
import jakarta.persistence.CascadeType;
import com.evan.model.GameType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



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
	
	@Column(name="[weight]")
	private Integer weight;
	
	@Column(name="[status]")
	private Integer status;
	
	
	@ManyToOne(fetch = FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "typeId")
	private GameType gameType;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon", cascade = CascadeType.ALL)
	private Set<CouponLog> couponLogs;
	
	public Integer getCouponId() {
		return couponId;
	}


	public Integer getWeight() {
		return weight;
	}


	public void setWeight(Integer weight) {
		this.weight = weight;
	}


	public Integer getStatus() {
		return status;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public Set<CouponLog> getCouponLogs() {
		return couponLogs;
	}


	public void setCouponLogs(Set<CouponLog> couponLogs) {
		this.couponLogs = couponLogs;
	}


	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}


	public Integer getTypeId() {
		return typeId;
	}


	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}


	public Float getCoupon() {
		return coupon;
	}


	public void setCoupon(Float coupon) {
		this.coupon = coupon;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public GameType getGameType() {
		return gameType;
	}


	public void setGameType(GameType gameType) {
		this.gameType = gameType;
	}


	
	public Coupon() {}

}
