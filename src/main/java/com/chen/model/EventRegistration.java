package com.chen.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity 
@Table(name = "eventRegistration")
public class EventRegistration {

	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "signupId")
	private Integer signupId;
	
	@Column(name = "eventId")
	private Integer eventId;
	
	@Column(name = "memberId")
	private Integer memberId;
	
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
	
	//@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "eventId",insertable=false, updatable=false)
	private Event event;
	
	@PrePersist
	public void onCreate() {
		if (signupDate == null) {
			signupDate = new Date();
		}
	}
}
