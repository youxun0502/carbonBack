package com.evan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evan.dao.GameRepository;
import com.evan.dao.GameTypeRepository;
import com.evan.dto.GameDTO;
import com.evan.dto.TypeDTO;
import com.evan.utils.ConvertToDTO;

@Service
public class GameTypeService {

	@Autowired
	private GameTypeRepository gtRespo;
	@Autowired
	private GameRepository gRespo;
	
	@Autowired
	private ConvertToDTO ctDTO;
	
	//===============================抓全部===========================
	public List<TypeDTO> getAllTypeInfo(){ return ctDTO.outputTypeDTOList(gtRespo.findAll());}

	public List<GameDTO> findGameByTypeName(String typeName) {
		return ctDTO.outputGameDTOList(gRespo.findByGameTypesTypeName(typeName));
	}
	
}
