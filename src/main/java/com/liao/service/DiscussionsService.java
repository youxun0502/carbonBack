package com.liao.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.liao.model.Discussions;
import com.liao.model.DiscussionsReposity;

@Service
public class DiscussionsService {
	
	@Autowired
	private DiscussionsReposity dRepo;
	
	public void insert(Discussions discussions) {
		dRepo.save(discussions);
	}
	
	public Discussions findById(Integer articleId) {
		Optional<Discussions> optional = dRepo.findById(articleId);
		
		if(optional.isPresent()) {
			return optional.get();
		}
		
		return null;
	}
	
	
	public void deleteById(Integer articleId) {
		dRepo.deleteById(articleId);
	}
	
	public List<Discussions> findAll(){
		return dRepo.findAll();
	}
	
	@Transactional
	public Discussions updateDiscussionsById(Integer articleId, Integer memberId, Integer eventId, String userName, Integer gameId, String gameName, String title, String dcontent, String lastReplyTime, Integer d_views, String dcreated_at, Integer dlikes , MultipartFile photoFile) throws IOException {
		Optional<Discussions> optional = dRepo.findById(articleId);
		
		if(optional.isPresent()) {
			Discussions discussions = optional.get();
			discussions.setMemberId(memberId);
			discussions.setEventId(eventId);
			discussions.setUserName(userName);
			discussions.setGameId(gameId);
			discussions.setGameName(gameName);
			discussions.setTitle(title);
			discussions.setDcontent(dcontent);
			discussions.setLastReplyTime(lastReplyTime);
			discussions.setD_views(d_views);
			discussions.setDcreated_at(dcreated_at);
			discussions.setDlikes(dlikes);
			discussions.setPhotoFile(photoFile.getBytes());
			return discussions;
		}
		
		System.out.println("no update data");
		
		return null;
	}
	
	public List<Discussions> findDiscussionsByUserName(String userName) {
        return dRepo.findDiscussionsByUserName(userName);
    }
	
	public List<Discussions> findDiscussionsByGameName(String gameName) {
        return dRepo.findDiscussionsByGameName(gameName);
    }
	
	public List<Discussions> findDiscussionsByTitle(String title) {
        return dRepo.findDiscussionsByTitle(title);
    }
	
	public Page<Discussions> findByPage(Integer pageNumber){
		Pageable pgb = PageRequest.of(pageNumber-1, 5, Sort.Direction.DESC, "lastReplyTime");
		
		Page<Discussions> page = dRepo.findAll(pgb);
		
		return page;
	}
	
}
	

	

