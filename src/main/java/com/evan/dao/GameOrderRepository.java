package com.evan.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import com.evan.model.GameOrder;

public interface GameOrderRepository extends JpaRepository<GameOrder, Integer> {




}
