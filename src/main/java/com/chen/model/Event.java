package com.chen.model;

import java.util.List;

import com.evan.model.Game;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GAMEID", insertable = false, updatable = false)
	private Game game;
	
	@Column(name = "gameId")
	private Integer gameId;
	
	@Lob
	@Column(name = "photo")
	private byte[] photo;
	
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
	
	/*
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "event",cascade = CascadeType.ALL)
	private List<EventRegistration> eventRegistration;
	*/
}
