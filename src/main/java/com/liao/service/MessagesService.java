package com.liao.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.liao.model.Messages;
import com.liao.model.MessagesReposity;

@Service
public class MessagesService {
	
	@Autowired
	private MessagesReposity mRepo;
	
	public void insert(Messages msg) {
		mRepo.save(msg);
	}
	
	public Messages findById(Integer messageId) {
		Optional<Messages> optional = mRepo.findById(messageId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	
	public void deleteById(Integer messageId) {
		mRepo.deleteById(messageId);
	}
	
	public List<Messages> findAll(){
		return mRepo.findAll();
	}
	
	@Transactional
	public Messages updateDiscussionsById(Integer messageId, Integer articleId, Integer memberId, String userName, Date mcreated_at, String mlikes, String mcontent, Integer gameId, String gameName, String mtitle, MultipartFile mphotoFile) throws IOException {
		Optional<Messages> optional = mRepo.findById(messageId);
		
		if(optional.isPresent()) {
			Messages msg = optional.get();
			msg.setArticleId(articleId);
			msg.setMemberId(memberId);
			msg.setUserName(userName);
			msg.setMcreated_at(mcreated_at);
			msg.setMlikes(mlikes);
			msg.setMcontent(mcontent);
			msg.setGameId(gameId);
			msg.setGameName(gameName);
			msg.setMtitle(mtitle);
			msg.setMphotoFile(mphotoFile.getBytes());
			
			return msg;
		}
		
		System.out.println("no update data");
		
		return null;
	}
	
	public List<Messages> findMessagesByUserName(String userName) {
        return mRepo.findMessagesByUserName(userName);
    }
	
	public List<Messages> findMessagesByTitle(String mtitle) {
        return mRepo.findMessagesByTitle(mtitle);
    }
	
	public List<Messages> findMessagesByGameName(String gameName) {
        return mRepo.findMessagesByGameName(gameName);
    }
	
}
	

	

