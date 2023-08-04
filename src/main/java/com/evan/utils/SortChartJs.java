package com.evan.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.evan.dao.GameRepository;
import com.evan.dto.GameDTO;
import com.evan.dto.TypeDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.annotation.PostConstruct;
import lombok.Data;
@Component
public class SortChartJs {

	ConvertToDTO convertToDTO = new ConvertToDTO();
	
	@Autowired
	private GameRepository gRepo;

	private String firstType;
	private Integer firstTypeNum;
	private Integer firstSecondTypeNum;
	private String secondType;
	private Integer secondTypeNum;
	private Integer firstThirdTypeNum;
	private String thirdType;
	private Integer thirdTypeNum;
	private Integer secondthirdTypeNum;
	private Integer firstsecondthirdTypeNum;
	private Integer allNum ;

	private List<TypeDTO> typeList;

	@JsonIgnore
	private List<GameDTO> gameList;

	public  SortChartJs() {
	}
	
	public void init(List<TypeDTO> typeList,int choose) {
		this.typeList = typeList;
		sortAndExtractTopThree(choose);
		calculateOtherSalesRev(choose);
	}
	
	public void sortAll(List<TypeDTO> typeList) {
		this.typeList = typeList;
		sortAndExtractTopThree(3);
	}

	
	public void sortGameDTOAll(List<GameDTO> gameList) {
		this.gameList = gameList;
		Collections.sort(gameList, Comparator.comparingDouble(GameDTO::getBuyerCount).reversed());
	}

	private void sortAndExtractTopThree(int choose) {

		switch (choose) {
		case 1:
			Collections.sort(typeList, Comparator.comparingDouble(TypeDTO::getTotalSalesRev).reversed());
			break;
		case 2:
			Collections.sort(typeList, Comparator.comparingDouble(TypeDTO::getGameNum).reversed());
			break;
		case 3:
			Collections.sort(typeList, Comparator.comparingDouble(TypeDTO::getTypeBuyer).reversed());
			break;
		default:
			Collections.sort(typeList, Comparator.comparingDouble(TypeDTO::getTotalSalesRev).reversed());
			break;
		}
		// 提取前三名的 typeName
		if (typeList.size() > 0) {
			firstType = typeList.get(0).getTypeName();
		}
		if (typeList.size() > 1) {
			secondType = typeList.get(1).getTypeName();
		}
		if (typeList.size() > 2) {
			thirdType = typeList.get(2).getTypeName();
		}
	}


	private void calculateOtherSalesRev(int choose) {
		switch (choose) {
		case 1:
			firstTypeNum = typeList.get(0).getTotalSalesRev();
			secondTypeNum = typeList.get(1).getTotalSalesRev();
			thirdTypeNum = typeList.get(2).getTotalSalesRev();

			repeatDeal(typeList.get(0).getTypeName(), typeList.get(1).getTypeName(), typeList.get(2).getTypeName(), 1);
			break;
		case 2:
			firstTypeNum = Integer.valueOf(typeList.get(0).getGameNum());
			secondTypeNum = Integer.valueOf(typeList.get(1).getGameNum());
			thirdTypeNum = Integer.valueOf(typeList.get(2).getGameNum());

			repeatDeal(typeList.get(0).getTypeName(), typeList.get(1).getTypeName(), typeList.get(2).getTypeName(), 2);
			break;
		case 3:
			firstTypeNum = Integer.valueOf(typeList.get(0).getTypeBuyer());
			secondTypeNum = Integer.valueOf(typeList.get(1).getTypeBuyer());
			thirdTypeNum = Integer.valueOf(typeList.get(2).getTypeBuyer());
			repeatDeal(typeList.get(0).getTypeName(), typeList.get(1).getTypeName(), typeList.get(2).getTypeName(),3);

			break;
		default:
			firstTypeNum = typeList.get(0).getTotalSalesRev();
			secondTypeNum = typeList.get(1).getTotalSalesRev();
			thirdTypeNum = typeList.get(2).getTotalSalesRev();
			repeatDeal(typeList.get(0).getTypeName(), typeList.get(1).getTypeName(), typeList.get(2).getTypeName(), 1);

			break;
		}
	}



	private void repeatDeal(String first,String second,String third,int choose) {
		List<GameDTO> fAnds = convertToDTO.outputGameDTOList(gRepo.findGamesByTypes(first,second));
		List<GameDTO> fAndt = convertToDTO.outputGameDTOList(gRepo.findGamesByTypes(first,third));
		List<GameDTO> sAndt = convertToDTO.outputGameDTOList(gRepo.findGamesByTypes(second,third));
		List<GameDTO> fAndsAndt = convertToDTO.outputGameDTOList(gRepo.findGamesByTypes( first,second, third));
		List<GameDTO> all = convertToDTO.outputGameDTOList(gRepo.findAll());
		System.out.println(all.size());

		switch (choose) {
		case 1:
			firstSecondTypeNum=(Integer) 0;
			firstThirdTypeNum = (Integer) 0;
			secondthirdTypeNum =(Integer) 0;
			firstsecondthirdTypeNum = (Integer)0;
			allNum = (Integer)0;

			for (GameDTO gameDTO : fAnds) {
				
				firstSecondTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : fAndt) {
				firstThirdTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : sAndt) {
				secondthirdTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : fAndsAndt) {
				firstsecondthirdTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : all) {
				allNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			break;
		case 2:
			firstSecondTypeNum=(Integer) fAnds.size();
			firstThirdTypeNum = (Integer) fAndt.size();
			secondthirdTypeNum =(Integer) sAndt.size();
			firstsecondthirdTypeNum =(Integer) fAndsAndt.size();
			allNum =(Integer) all.size();

			break;
		case 3:
			firstSecondTypeNum=(Integer) 0;
			firstThirdTypeNum = (Integer) 0;
			secondthirdTypeNum =(Integer) 0;
			firstsecondthirdTypeNum = (Integer)0;
			allNum = (Integer)0;
			for (GameDTO gameDTO : fAnds) {
				firstSecondTypeNum += gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : fAndt) {
				firstThirdTypeNum += gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : sAndt) {
				secondthirdTypeNum += gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : fAndsAndt) {
				firstsecondthirdTypeNum += gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : all) {
				allNum += gameDTO.getBuyerCount();
			}
			break;

		default:
			firstSecondTypeNum=(Integer) 0;
			firstThirdTypeNum = (Integer) 0;
			secondthirdTypeNum =(Integer) 0;
			firstsecondthirdTypeNum = (Integer)0;
			allNum = (Integer)0;
			for (GameDTO gameDTO : fAnds) {
				firstSecondTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : fAndt) {
				firstThirdTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : sAndt) {
				secondthirdTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : fAndsAndt) {
				firstsecondthirdTypeNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			for (GameDTO gameDTO : all) {
				allNum += gameDTO.getPrice()*gameDTO.getBuyerCount();
			}
			break;
		}
	}

	public String getFirstType() {
		return firstType;
	}

	public void setFirstType(String firstType) {
		this.firstType = firstType;
	}

	public Integer getFirstTypeNum() {
		return firstTypeNum;
	}

	public void setFirstTypeNum(Integer firstTypeNum) {
		this.firstTypeNum = firstTypeNum;
	}

	public Integer getFirstSecondTypeNum() {
		return firstSecondTypeNum;
	}

	public void setFirstSecondTypeNum(Integer firstSecondTypeNum) {
		this.firstSecondTypeNum = firstSecondTypeNum;
	}

	public String getSecondType() {
		return secondType;
	}

	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}

	public Integer getSecondTypeNum() {
		return secondTypeNum;
	}

	public void setSecondTypeNum(Integer secondTypeNum) {
		this.secondTypeNum = secondTypeNum;
	}

	public Integer getFirstThirdTypeNum() {
		return firstThirdTypeNum;
	}

	public void setFirstThirdTypeNum(Integer firstThirdTypeNum) {
		this.firstThirdTypeNum = firstThirdTypeNum;
	}

	public String getThirdType() {
		return thirdType;
	}

	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

	public Integer getThirdTypeNum() {
		return thirdTypeNum;
	}

	public void setThirdTypeNum(Integer thirdTypeNum) {
		this.thirdTypeNum = thirdTypeNum;
	}

	public Integer getSecondthirdTypeNum() {
		return secondthirdTypeNum;
	}

	public void setSecondthirdTypeNum(Integer secondthirdTypeNum) {
		this.secondthirdTypeNum = secondthirdTypeNum;
	}

	public Integer getFirstsecondthirdTypeNum() {
		return firstsecondthirdTypeNum;
	}

	public void setFirstsecondthirdTypeNum(Integer firstsecondthirdTypeNum) {
		this.firstsecondthirdTypeNum = firstsecondthirdTypeNum;
	}

	public Integer getAllNum() {
		return allNum;
	}

	public void setAllNum(Integer allNum) {
		this.allNum = allNum;
	}
	
	public List<TypeDTO> getTypeList() {
		return typeList;
	}

	public List<GameDTO> getGameList() {
		return gameList;
	}

}
