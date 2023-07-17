package com.liu.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.liu.model.Chat;
import com.liu.model.ChatRepository;
import com.liu.model.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatService {

	@Autowired
	ChatRepository chatRepository;
	
	@Autowired
	MemberRepository memberRepository;
	
	public Chat insertChatLog(Chat chat) {
		return chatRepository.save(chat);
		
	}
	
	public List<Chat> findLastTenChat(Integer from, Integer to){
		Pageable pgb = PageRequest.of(0, 10);
		Page<Chat> chatPage = chatRepository.findLastTenChat(from, to, pgb);
		List<Chat> chatList = chatPage.getContent();
		return chatList;
	}
	
	@Transactional
	public void readAllMessage(Integer to, Integer from) {
		List<Chat> chats = chatRepository.findNoReadChat(to, from);
		for (Chat chat : chats) {
			chat.setIsRead(1);
		}
	}
	
	public Integer findNotReadMessage(Integer from) {
		return chatRepository.findNotReadByMemberId(from);
	}
}
