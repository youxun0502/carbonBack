package com.evan.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.evan.dao.GameRepository;
import com.evan.dto.GameDTO;
import com.evan.dto.TypeDTO;

import jakarta.annotation.PostConstruct;
import lombok.Data;
@Component
public class SortChartJs {


	
	ConvertToDTO convertToDTO = new ConvertToDTO();
	
	@Autowired
	private GameRepository gRepo;

	private String firstType;
	private Float firstTypeNum;
	private Float firstSecondTypeNum;
	private String secondType;
	private Float secondTypeNum;
	private Float firstThirdTypeNum;
	private String thirdType;
	private Float thirdTypeNum;
	private Float secondthirdTypeNum;
	private Float firstsecondthirdTypeNum;
	private Float allNum ;
	private List<TypeDTO> typeList;

	public  SortChartJs() {
	}
	
	public void init(List<TypeDTO> typeList,int choose) {
		this.typeList = typeList;
		sortAndExtractTopThree(choose);
		calculateOtherSalesRev(choose);
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
			firstTypeNum = Float.valueOf(typeList.get(0).getGameNum());
			secondTypeNum = Float.valueOf(typeList.get(1).getGameNum());
			thirdTypeNum = Float.valueOf(typeList.get(2).getGameNum());

			repeatDeal(typeList.get(0).getTypeName(), typeList.get(1).getTypeName(), typeList.get(2).getTypeName(), 2);
			break;
		case 3:
			firstTypeNum = Float.valueOf(typeList.get(0).getTypeBuyer());
			secondTypeNum = Float.valueOf(typeList.get(1).getTypeBuyer());
			thirdTypeNum = Float.valueOf(typeList.get(2).getTypeBuyer());
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
			firstSecondTypeNum=(float) 0;
			firstThirdTypeNum = (float) 0;
			secondthirdTypeNum =(float) 0;
			firstsecondthirdTypeNum = (float)0;
			allNum = (float)0;

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
			firstSecondTypeNum=(float) fAnds.size();
			firstThirdTypeNum = (float) fAndt.size();
			secondthirdTypeNum =(float) sAndt.size();
			firstsecondthirdTypeNum =(float) fAndsAndt.size();
			allNum =(float) all.size();

			break;
		case 3:
			firstSecondTypeNum=(float) 0;
			firstThirdTypeNum = (float) 0;
			secondthirdTypeNum =(float) 0;
			firstsecondthirdTypeNum = (float)0;
			allNum = (float)0;
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
			firstSecondTypeNum=(float) 0;
			firstThirdTypeNum = (float) 0;
			secondthirdTypeNum =(float) 0;
			firstsecondthirdTypeNum = (float)0;
			allNum = (float)0;
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

	public Float getFirstTypeNum() {
		return firstTypeNum;
	}

	public void setFirstTypeNum(Float firstTypeNum) {
		this.firstTypeNum = firstTypeNum;
	}

	public Float getFirstSecondTypeNum() {
		return firstSecondTypeNum;
	}

	public void setFirstSecondTypeNum(Float firstSecondTypeNum) {
		this.firstSecondTypeNum = firstSecondTypeNum;
	}

	public String getSecondType() {
		return secondType;
	}

	public void setSecondType(String secondType) {
		this.secondType = secondType;
	}

	public Float getSecondTypeNum() {
		return secondTypeNum;
	}

	public void setSecondTypeNum(Float secondTypeNum) {
		this.secondTypeNum = secondTypeNum;
	}

	public Float getFirstThirdTypeNum() {
		return firstThirdTypeNum;
	}

	public void setFirstThirdTypeNum(Float firstThirdTypeNum) {
		this.firstThirdTypeNum = firstThirdTypeNum;
	}

	public String getThirdType() {
		return thirdType;
	}

	public void setThirdType(String thirdType) {
		this.thirdType = thirdType;
	}

	public Float getThirdTypeNum() {
		return thirdTypeNum;
	}

	public void setThirdTypeNum(Float thirdTypeNum) {
		this.thirdTypeNum = thirdTypeNum;
	}

	public Float getSecondthirdTypeNum() {
		return secondthirdTypeNum;
	}

	public void setSecondthirdTypeNum(Float secondthirdTypeNum) {
		this.secondthirdTypeNum = secondthirdTypeNum;
	}

	public Float getFirstsecondthirdTypeNum() {
		return firstsecondthirdTypeNum;
	}

	public void setFirstsecondthirdTypeNum(Float firstsecondthirdTypeNum) {
		this.firstsecondthirdTypeNum = firstsecondthirdTypeNum;
	}

	public Float getAllNum() {
		return allNum;
	}

	public void setAllNum(Float allNum) {
		this.allNum = allNum;
	}
	
	
	
}
