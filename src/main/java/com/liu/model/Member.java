package com.liu.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.evan.model.Game;
import com.evan.model.GameOrder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ni.model.GameItem;
import com.ni.model.Wallet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "member")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "userId")
	private String userId;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "memberPwd")
	private String memberPwd;

	@Column(name = "memberName")
	private String memberName;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "birthday")
	private Date birthday;
	
	@Column(name = "gender")
	private Integer gender;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "levelId", insertable = false, updatable = false)
	private Integer levelId;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "useAvatar")
	private Integer useAvatar;
	@Column(name = "useFrame")
	private Integer useFrame;
	@Column(name = "useBackground")
	private Integer useBackground;
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")// 回傳到前端的格式
	@DateTimeFormat(pattern = "yyyy-MM-dd") // java的格式
	@Temporal(TemporalType.DATE)// 資料庫的型別
	@Column(name = "registrationDate")
	private Date registrationDate;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "membercart", 
	    joinColumns = {@JoinColumn(name = "MEMBERID")},
	    inverseJoinColumns = {@JoinColumn(name = "GAMEID")})
	private Set<Game> games = new HashSet<>();
	
	// 物件轉換成 Persistent 狀態以前做以下事情
	@PrePersist
	public void onCreate() {
		if(registrationDate == null ) {
			registrationDate = new Date();
		}
	}
	
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "levelId")
	private Level level;
	
	private Integer status;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL)
	private Set<CouponLog> couponLogs;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<GameOrder> gameOrder = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade = CascadeType.ALL)
	private List<Wallet> wallets = new ArrayList<>();
	
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Member() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMemberPwd() {
		return memberPwd;
	}

	public void setMemberPwd(String memberPwd) {
		this.memberPwd = memberPwd;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Integer getUseAvatar() {
		return useAvatar;
	}

	public void setUseAvatar(Integer useAvatar) {
		this.useAvatar = useAvatar;
	}

	public Integer getUseFrame() {
		return useFrame;
	}

	public void setUseFrame(Integer useFrame) {
		this.useFrame = useFrame;
	}

	public Integer getUseBackground() {
		return useBackground;
	}

	public void setUseBackground(Integer useBackground) {
		this.useBackground = useBackground;
	}
	
	public Set<Game> getGames() {
		return games;
	}

	public List<GameOrder> getGameOrder() {
		return gameOrder;
	}

	public void setGameOrder(List<GameOrder> gameOrder) {
		this.gameOrder = gameOrder;
	}

	public List<Wallet> getWallets() {
		return wallets;
	}

	public void setWallets(List<Wallet> wallets) {
		this.wallets = wallets;
	}

	


}