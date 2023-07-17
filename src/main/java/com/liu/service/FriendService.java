package com.liu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liu.model.Friend;
import com.liu.model.FriendRepository;

@Service
public class FriendService {

	@Autowired
	FriendRepository friendRepository;
	
	
	public List<Friend> findFriendByuserId(Integer userId){
		return friendRepository.findFriendByUserId(userId);
	}
}
