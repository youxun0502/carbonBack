package com.liu.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("from Member where memberName like %:memberName%")
	public List<Member> findMemberByName(@Param("memberName") String memberName);

	@Query("from Member where email = :email ")
	public Member findMemberByEmail(@Param("email") String email);

	@Query("from Member where phone = :phone ")
	public Member findMemberByPhone(@Param("phone") String phone);

	@Query(nativeQuery = true, value = "select month(registrationDate) registrationMonth, count(*) count  from member group by month(registrationDate)")
	public List<Map<String, Integer>> findRegistrationMonth();
}