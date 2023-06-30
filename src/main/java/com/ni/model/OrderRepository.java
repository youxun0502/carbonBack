package com.ni.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<ItemOrder, Integer> {

	@Query("FROM ItemOrder o JOIN GameItem i ON o.itemId = i.itemId "
			+ "JOIN Game g ON i.gameId = g.gameId WHERE i.gameId = :id AND itemName = :name "
			+ "AND buyer IS NULL AND seller IS NOT NULL AND o.status = 1")
	public List<ItemOrder> findSellItemList(@Param("id") Integer id, @Param("name") String name);
	
	@Query("SELECT o.itemId, i.itemName, i.itemImgName, g.gameId, MIN(o.price) price FROM ItemOrder o "
			+ "JOIN GameItem i ON o.itemId = i.itemId JOIN Game g ON i.gameId = g.gameId "
			+ "WHERE o.status = 1 AND buyer IS NULL AND seller IS NOT NULL " // will delete this line
			+ "GROUP BY o.itemId, i.itemName, i.itemImgName, g.gameId")
	public List<Object[]> countOrderList();
	
	@Query("FROM ItemOrder WHERE itemId = :id AND buyer IS NOT NULL AND seller IS NOT NULL AND status = 2")
	public List<ItemOrder> findByItemIdAndStatus(@Param("id") Integer id);
	
	@Query(value = "SELECT TOP 1 * FROM itemOrder WHERE itemId = :id AND buyer IS NOT NULL "
			+ "AND seller IS NULL AND status = 1 ORDER BY price DESC, ordId", nativeQuery = true)
	public List<ItemOrder> findBuysByIdAndStatus(@Param("id") Integer id);
	
	@Query("FROM ItemOrder WHERE itemId = :id AND buyer IS NULL AND seller IS NOT NULL AND status = 1 ORDER BY price, ordId")
	public List<ItemOrder> findSalesByIdAndStatus(@Param("id") Integer id);
}
