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
@Table(name = "chattingRoomLog")
public class Chat {
	

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Column(name = "chattingRoomLogId")
		private Integer chattingRoomLogId;
		
		@Column(name = "[fromMember]",insertable = false,updatable = false)
		private Integer fromMember;
		
		@Column(name = "[toMember]", insertable = false, updatable = false)
		private Integer toMember;
			
		@Column(name="content")
		private String content;
		
		@JoinColumn(name="isRead")
		private Integer isRead;
		
		@ManyToOne
		@JoinColumn(name = "[fromMember]")
		private  Member chatFromMember;
		
		@ManyToOne
		@JoinColumn(name = "[toMember]")
		private Member chatToMember;

		public Integer getChattingRoomLogId() {
			return chattingRoomLogId;
		}

		public void setChattingRoomLogId(Integer chattingRoomLogId) {
			this.chattingRoomLogId = chattingRoomLogId;
		}

		public Integer getFromMember() {
			return fromMember;
		}

		public void setFromMember(Integer fromMember) {
			this.fromMember = fromMember;
		}

		public Integer getToMember() {
			return toMember;
		}

		public void setToMember(Integer toMember) {
			this.toMember = toMember;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}



		public Member getChatFromMember() {
			return chatFromMember;
		}

		public void setChatFromMember(Member chatFromMember) {
			this.chatFromMember = chatFromMember;
		}

		public Member getChatToMember() {
			return chatToMember;
		}

		public void setChatToMember(Member chatToMember) {
			this.chatToMember = chatToMember;
		}

		public Integer getIsRead() {
			return isRead;
		}

		public void setIsRead(Integer isRead) {
			this.isRead = isRead;
		}


}
