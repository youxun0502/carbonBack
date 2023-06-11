package com.liu.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("from Member where memberName like %:memberName%")
	public List<Member> findMemberByName(@Param("memberName") String memberName);
	
	@Query("from Member where email = :email And memberPwd = :memberPwd ")
	public Member isMember(@Param("email") String email, @Param("memberPwd") String memberPwd);
	
	@Query("from Member where email = :email ")
	public Member emailAlreadyRegistered(@Param("email") String email);
	
	@Query("from Member where phone = :phone ")
	public Member phoneAlreadyRegistered(@Param("phone") String phone);
}