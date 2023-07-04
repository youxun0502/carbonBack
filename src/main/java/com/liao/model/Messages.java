package com.liao.model;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Entity
@Table(name = "message")
public class Messages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "messageId")
	private Integer messageId;
	
	@Lob
	@Column(name="mphotoFile")
	private byte[] mphotoFile;
	
	@Column(name = "articleId", insertable = false, updatable = false)
	private Integer articleId;
	
	@Column(name = "memberId")
	private Integer memberId;
	
	@Column(name = "userName")
	private String userName;

	
	@JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss EEEE",timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "mcreated_at")
	private Date mcreated_at;

	@PrePersist
	public void onCreate() {
		if (mcreated_at == null) {
			mcreated_at = new Date();
		}
	}
	
	
	@Column(name = "mlikes")
	private String mlikes;
	
	@Column(name = "mcontent")
	private String mcontent;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId", referencedColumnName = "articleId")
    private Discussions discussion;
	
	
	public Messages() {
	}

}
