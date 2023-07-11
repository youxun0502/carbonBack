package com.ni.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

	@Query("FROM Wallet WHERE memberId = :id ORDER BY id DESC")
	public List<Wallet> findByMemberId(@Param("id") Integer id);
	
	@Query(value = "SELECT * FROM (SELECT *, ROW_NUMBER() OVER(PARTITION BY memberId ORDER BY id DESC) sn "
			+ "FROM wallet WHERE memberId = :id ) r WHERE r.sn = 1", nativeQuery = true)
	public Wallet findBalance(@Param("id") Integer id);
}
