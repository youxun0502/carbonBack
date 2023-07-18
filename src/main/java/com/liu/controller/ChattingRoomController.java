package com.liu.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.liu.config.Message;
import com.liu.dto.ChatDto;
import com.liu.model.Chat;
import com.liu.model.Member;
import com.liu.service.ChatService;
import com.liu.service.MemberService;

@Controller
public class ChattingRoomController {

	@Autowired
	MemberService mService;
	
	@Autowired
	ChatService chatService;
	
	@ResponseBody
	@PostMapping("/chattingRoom/insert")
	public Message messageIntoDataBase(@RequestBody Message message){
		Member fromMember = mService.findById(Integer.parseInt(message.getFrom()));
		Member toMember = mService.findById(Integer.parseInt(message.getTo()));
		
		Chat chat = new Chat();
		chat.setChatFromMember(fromMember);
		chat.setChatToMember(toMember);
		chat.setContent(message.getContent());
		chat.setIsRead(2);
		chatService.insertChatLog(chat);
		
		return message;
	}
	@ResponseBody
	@GetMapping("/chattingRoom/findLastTenMessage")
	public List<ChatDto> findLastTenMessage(@RequestParam("from") String from, @RequestParam("to") String to){
		
		List<Chat> lastTenChat = chatService.findLastTenChat(Integer.parseInt(from), Integer.parseInt(to));
		
	
		List<ChatDto> tenChatDto = new ArrayList<>();
		for (Chat chat : lastTenChat) {
			ChatDto chatDto = new ChatDto();
			chatDto.setToMember(chat.getToMember());
			chatDto.setFromMember(chat.getFromMember());
			chatDto.setContent(chat.getContent());
			tenChatDto.add(chatDto);
		}
		if(tenChatDto.size()!=0) {
			Collections.reverse(tenChatDto);
			return tenChatDto;
		}else {
			return null;
		}
		
	}
	
	@ResponseBody
	@PutMapping("/chattingRoom/readAllMessage")
	public void readAllMessage(@RequestBody Map<String,Integer> member) {

		Integer to = member.get("to"); 
		Integer from = member.get("from"); 
		System.out.println("寄給誰"+to); //1
		System.out.println("誰寄的"+from); //2
		
		chatService.readAllMessage(to, from);
	}
}
