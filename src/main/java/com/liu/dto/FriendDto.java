package com.liu.dto;


public class FriendDto {

	private Integer friendLogId;
	

	private Integer inviter;
	
	
	private Integer recipient;
		

	private Integer isFriend;
	
	private Integer messageNotRead;
	

	public Integer getMessageNotRead() {
		return messageNotRead;
	}

	public void setMessageNotRead(Integer messageNotRead) {
		this.messageNotRead = messageNotRead;
	}

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

}
