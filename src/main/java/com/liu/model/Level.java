package com.liu.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "level")
public class Level {
	
	@Id
	@Column(name = "levelId")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer levelId;
	
	@Column(name = "[desc]")
	private String desc;
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "level", cascade = CascadeType.ALL)
	private Set<Member> member;
	
	public Level() {
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Set<Member> getMember() {
		return member;
	}

	public void setMember(Set<Member> member) {
		this.member = member;
	}

}