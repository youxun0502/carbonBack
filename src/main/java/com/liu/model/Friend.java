package com.liu.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "friendLog")
public class Friend {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "friendLogId")
	private Integer friendLogId;
	
	@Column(name = "inviter",insertable = false,updatable = false)
	private Integer inviter;
	
	@Column(name = "recipient", insertable = false, updatable = false)
	private Integer recipient;
		
	@Column(name="isFriend")
	private Integer isFriend;
	
	@ManyToOne
	@JoinColumn(name = "inviter")
	private  Member inviterMember;
	
	@ManyToOne
	@JoinColumn(name = "recipient")
	private Member recipientMember;

	public Integer getFriendLogId() {
		return friendLogId;
	}

	public void setFriendLogId(Integer friendLogId) {
		this.friendLogId = friendLogId;
	}

	public Integer getInviter() {
		return inviter;
	}

	public void setInviter(Integer inviter) {
		this.inviter = inviter;
	}

	public Integer getRecipient() {
		return recipient;
	}

	public void setRecipient(Integer recipient) {
		this.recipient = recipient;
	}

	public Integer getIsFriend() {
		return isFriend;
	}

	public void setIsFriend(Integer isFriend) {
		this.isFriend = isFriend;
	}

	public Member getInviterMember() {
		return inviterMember;
	}

	public void setInviterMember(Member inviterMember) {
		this.inviterMember = inviterMember;
	}

	public Member getRecipientMember() {
		return recipientMember;
	}

	public void setRecipientMember(Member recipientMember) {
		this.recipientMember = recipientMember;
	}
	
	
}
