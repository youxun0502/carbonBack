package com.chen.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "eventId")
	private Integer eventId;
	
	@Column(name = "gameId")
	private Integer gameId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "startDate")
	private String startDate;
	
	@Column(name = "endDate")
	private String endDate;
	
	@Column(name = "timeLimitedDiscount")
	private String timeLimitedDiscount;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "quotaLimited")
	private Integer quotaLimited;
	
	@Column(name = "deadline")
	private String deadline;
	
	@Column(name = "fee")
	private Integer fee;
}
