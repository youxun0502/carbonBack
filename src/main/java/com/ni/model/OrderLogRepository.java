package com.ni.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderLogRepository extends JpaRepository<OrderLog, Integer> {

	@Query("FROM OrderLog o JOIN GameItem i ON o.itemId = i.itemId "
			+ "JOIN Game g ON i.gameId = g.gameId WHERE i.gameId = :id AND itemName = :name "
			+ "AND buyer IS NULL AND seller IS NOT NULL AND o.status = 1")
	public List<OrderLog> findSellItemList(Integer id, String name);
	
	@Query("SELECT o.itemId, i.itemName, i.itemImgName, g.gameId, MIN(o.price) price FROM OrderLog o "
			+ "JOIN GameItem i ON o.itemId = i.itemId JOIN Game g ON i.gameId = g.gameId "
			+ "WHERE o.status = 1 GROUP BY o.itemId, i.itemName, i.itemImgName, g.gameId")
	public List<Object[]> countOrderList();
}
