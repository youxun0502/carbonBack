package com.liao.model;


import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "discussions")
public class Discussions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "articleId")
	private Integer articleId;
	
	@Lob
	@Column(name="photoFile")
	private byte[] photoFile;
	
	
	@Column(name = "memberId")
	private Integer memberId;
	
	@Column(name = "eventId")
	private Integer eventId;
	
	@Column(name = "userName")
	private String userName;
	
	@Column(name = "gameId")
	private Integer gameId;
	
	@Column(name = "gameName")
	private String gameName;

	@Column(name = "title")
	private String title;
	
	@Column(name = "dcontent")
	private String dcontent;
	
	@Column(name = "lastReplyTime")
	private String lastReplyTime;
	
	@Column(name = "d_views")
	private Integer d_views;
	
	@Column(name = "dcreated_at")
	private String dcreated_at;
	
	@Column(name = "dlikes")
	private Integer dlikes;
	
	@Column(name= "photoId")
	private Integer photoId;
	
//	@OneToMany(mappedBy = "discussion")
//	private List<Messages> messages;
	

	
	public Discussions() {
	}

}
