package com.liao.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.liao.model.Discussions;
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
	public Messages updateDiscussionsById(Integer messageId, Integer articleId, Integer memberId, String userName, Date mcreated, String mlikes, String mcontent, Integer gameId, String gameName, String mtitle, MultipartFile mphotoFile) throws IOException {
		Optional<Messages> optional = mRepo.findById(messageId);
		
		if(optional.isPresent()) {
			Messages msg = optional.get();
			msg.setArticleId(articleId);
			msg.setMemberId(memberId);
			msg.setUserName(userName);
			msg.setMcreated(mcreated);
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
	
	public Page<Messages> findByPage(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1, 5, Sort.Direction.DESC, "mcreated");
		
		Page<Messages> page = mRepo.findAll(pgb);
		
		return page;
	}
	
	
	@Transactional
    public int likeMessage(Integer messageId) {
        // 根據 messageId 從資料庫中獲取訊息
        Optional<Messages> optionalMessage = mRepo.findById(messageId);
        
        if (optionalMessage.isPresent()) {
            Messages message = optionalMessage.get();
            
            // 更新按讚數量
            int likeCount = Integer.parseInt(message.getMlikes()==null?"0":message.getMlikes());
            likeCount++;
            message.setMlikes(String.valueOf(likeCount));
            
            // 儲存更新後的訊息
            mRepo.save(message);
            
            // 返回按讚後的數量
            return likeCount;
        }
        
        // 若訊息不存在，返回 -1 或適當的錯誤碼
        return -1;
    }
	
}
	

	

