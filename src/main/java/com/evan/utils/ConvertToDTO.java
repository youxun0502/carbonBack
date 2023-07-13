package com.evan.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.evan.dao.GameRepository;
import com.evan.dao.GameTypeRepository;
import com.evan.dto.CartDTO;
import com.evan.dto.GameDTO;
import com.evan.dto.OrderDTO;
import com.evan.dto.OrderLogDTO;
import com.evan.dto.TypeDTO;
import com.evan.model.Game;
import com.evan.model.GameOrder;
import com.evan.model.GameOrderLog;
import com.evan.model.GamePhoto;
import com.evan.model.GameType;
import com.liu.model.Member;

@Component
public class ConvertToDTO {

	@Autowired
	private GameTypeRepository gtRepo;
	@Autowired
	private GameRepository gRepos;

	public List<GameDTO> outputGameDTOList(List<Game> games) {

		ArrayList<GameDTO> gameDTOList = new ArrayList<>();

		for (Game game : games) {
			GameDTO gameDTO = new GameDTO();
			if (game.getGameId() != null)
				gameDTO.setGameId(game.getGameId());
			if (game.getGameName() != null)
				gameDTO.setGameName(game.getGameName());
			if (game.getGameIntroduce() != null)
				gameDTO.setGameIntroduce(game.getGameIntroduce());
			if (game.getGamePhotoLists() != null)
				gameDTO.setGamePhotoLists(game.getGamePhotoLists());
			if (game.getGameTypes() != null)
				gameDTO.setGameTypes(game.getGameTypes());
			if (game.getCreateDate() != null)
				gameDTO.setCreateDate(game.getCreateDate());
			if (game.getBuyerCount() != null)
				gameDTO.setBuyerCount(game.getBuyerCount());
			if (game.getPrice() != null)
				gameDTO.setPrice(game.getPrice());
			if (game.getStatus() != null)
				gameDTO.setStatus(game.getStatus());
			gameDTOList.add(gameDTO);
		}
		return gameDTOList;
	}

	public List<TypeDTO> outputTypeDTOList(List<GameType> findAll) {
		ArrayList<TypeDTO> typeDTOList = new ArrayList<>();

		for (GameType Type : findAll) {
			TypeDTO typeDTO = new TypeDTO();
			if (Type.getTypeId() != null) {

				if (!Type.getTypeName().equals("") && Type.getGames().size() != 0) {
					typeDTO.setTypeId(Type.getTypeId());
					if (Type.getTypeName() != null)
						typeDTO.setTypeName(Type.getTypeName());
					if (Type.getGames() != null)
						typeDTO.setGames(Type.getGames());
					typeDTOList.add(typeDTO);
				} else {
					gtRepo.deleteById(Type.getTypeId());
				}
			}
		}
		return typeDTOList;
	}

	public List<CartDTO> ouptCartDTOs(Member member) {
		ArrayList<CartDTO> cartDTOs = new ArrayList<>();
		System.out.println("DTO");
		if(member.getGames().size()!= 0) {
		for (Game game : member.getGames()) {
			CartDTO cartDTO = new CartDTO();
			cartDTO.setGameName(game.getGameName());
			cartDTO.setMemberId(member.getId());
			if(game.getGamePhotoLists().get(0)!=null)cartDTO.setPhotoId(game.getGamePhotoLists().get(0).getPhotoId());
			cartDTO.setPrice(game.getPrice());
			cartDTO.setGameId(game.getGameId());
			cartDTOs.add(cartDTO);
			}
		}
		return cartDTOs;
	}

	public List<OrderDTO> outputOrderDTOList(List<GameOrder> gameOrders) {
		List<OrderDTO> orderDtos = new ArrayList<>();
		
//		gameOrders.remove(gameOrders.size()-1);
		for (GameOrder gameOrder : gameOrders) {
			OrderDTO orderDTO = new OrderDTO();

			List<OrderLogDTO> orderLogDtos = new ArrayList<>();
			int countPrice = 0;
			for (GameOrderLog log : gameOrder.getGameOrderLog()) {
				OrderLogDTO orderLogDTO = new OrderLogDTO();
				orderLogDTO.setGameName(log.getGameName());
				orderLogDTO.setPrice(log.getPriceLog());
				GamePhoto gamePhoto = gRepos.findGameByGameName(log.getGameName()).get(0).getGamePhotoLists().get(0);
				orderLogDTO.setPhotoId(gamePhoto.getPhotoId());
				countPrice += log.getPriceLog();
				orderLogDtos.add(orderLogDTO);
			}
			orderDTO.setTotalPrice(countPrice);
			orderDTO.setLogs(orderLogDtos);
			orderDTO.setCreateDate(gameOrder.getCreateTime());
			orderDTO.setOrderID(gameOrder.getOrderId());
			orderDTO.setStatus(gameOrder.getStatus());
			
			orderDtos.add(orderDTO);
		}
		return orderDtos;
	}
	
	

}
