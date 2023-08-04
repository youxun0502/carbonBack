package com.li.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BonusItemRepository extends JpaRepository<BonusItem, Integer> {

	@Query(value = "select * from bonusitem where bonusName like :name or bonusDes like :name", nativeQuery = true)
	public List<BonusItem> findByName(@Param("name") String name);
	
	@Query(value = "select * from bonusitem where bonusType = :bonusType", nativeQuery = true)
	public List<BonusItem> findByBonusType(@Param("bonusType") String bonusType);
	
	@Query(value = "select * from bonusitem where status = 'true' AND bonusType = :bonusType", nativeQuery = true)
	public Page<BonusItem> findByBonusTypePage(@Param("bonusType") String bonusType,Pageable page);
}
