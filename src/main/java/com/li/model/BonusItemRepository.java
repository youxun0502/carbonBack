package com.li.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BonusItemRepository extends JpaRepository<BonusItem, Integer> {

	@Query(value = "select * from bonusitem where bonusName like :name or bonusDes like :name", nativeQuery = true)
	public List<BonusItem> findByName(@Param("name") String name);
}
