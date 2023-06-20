package com.chen.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity 
@Table(name = "competitionRegistration")
public class CompetitionRegistration {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "signupId")
	private Integer signupId;
	
	@Column(name = "competitionId")
	private Integer competitionId;

	@Column(name = "memberId")
	private Integer memberId;
	
	@Column(name = "gameNickname")
	private String gameNickname;
	
	@Column(name = "teamName")
	private String teamName;
	
	@Column(name = "realName")
	private String realName;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "phone")
	private String phone;
	
	@Column(name = "address")
	private String address;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "signupDate")
	private Date signupDate;
	
	@PrePersist
	public void onCreate() {
		if (signupDate == null) {
			signupDate = new Date();
		}
	}
}
