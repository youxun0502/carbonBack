package com.liu.model;

import java.util.List;

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

	@Query(nativeQuery = true, value = "select month(registrationDate) registrationMonth, count(*) count  from member where year(registrationDate) = :year  group by month(registrationDate)")
	public List<Object[]> findRegistrationMonth(@Param("year") String year);

	@Query(nativeQuery = true, value = "select year(registrationDate) registrationYear from member group by year(registrationDate)")
	public List<String> findAllRegistrationYear();

	@Query(nativeQuery = true, value = "select count(*) count, gender gender from member where year(registrationDate) = :year  group by gender")
	public List<Object[]> findRegistrationGender(@Param("year") String year);

}