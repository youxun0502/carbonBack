package com.liu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Integer> {

	@Query("from Friend where (inviter = :userId or recipient = :userId) and isFriend = 1")
	public List<Friend> findFriendByUserId(@Param("userId") Integer userId);
}
