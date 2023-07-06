package com.ni.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ni.dto.WalletDTO;
import com.ni.model.Wallet;
import com.ni.model.WalletRepository;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepo;
	
	public List<Wallet> findAll() {
		return walletRepo.findAll();
	}
	
	public void insert(Wallet wallet) {
		walletRepo.save(wallet);
//		return convertToDTOList(wallet);
	}
	
	
//	======================= 轉換 DTO 和 Entity =======================
	public List<WalletDTO> convertToDTOList(List<Wallet> wallets) {
		List<WalletDTO> walletDTOs = new ArrayList<>();
		for(Wallet wallet : wallets) {
			WalletDTO walletDTO = new WalletDTO();
			if(wallet.getId() != null) walletDTO.setId(wallet.getId());
			if(wallet.getTradeNo() != null) walletDTO.setTradeNo(wallet.getTradeNo());;
			if(wallet.getMemberId() != null) walletDTO.setMemberId(wallet.getMemberId());;
			if(wallet.getChange() != null) walletDTO.setChange(wallet.getChange());
			if(wallet.getBalance() != null) walletDTO.setBalance(wallet.getBalance());
			if(wallet.getDesc() != null) walletDTO.setDesc(wallet.getDesc());
			if(wallet.getCreateTime() != null) walletDTO.setCreateTime(wallet.getCreateTime());
			if(wallet.getMember() != null) walletDTO.setMember(wallet.getMember());
			walletDTOs.add(walletDTO);
 		}
		return walletDTOs;
	}
}
