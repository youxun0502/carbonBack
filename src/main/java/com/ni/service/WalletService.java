package com.ni.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ni.dto.WalletDTO;
import com.ni.model.Wallet;
import com.ni.model.WalletRepository;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import ecpay.payment.integration.domain.QueryTradeInfoObj;

@Service
public class WalletService {

	@Autowired
	private WalletRepository walletRepo;
	
	public List<Wallet> findAll() {
		return walletRepo.findAll();
	}
	
	public WalletDTO findBalance(Integer memberId) {
		return convertToDTO(walletRepo.findBalance(memberId));
	}
	
	public WalletDTO insert(Wallet wallet) {
		Wallet balance = walletRepo.findBalance(wallet.getMemberId());
		if(balance != null) {
			wallet.setBalance(wallet.getChange() + balance.getBalance());
		} else {
			wallet.setBalance(wallet.getChange());
		}
		walletRepo.save(wallet);
		return convertToDTO(wallet);
	}
	
//	綠界
	public String ecpayCheckout(Map<String, Object> wallet) {
		
		String uuId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String dateString = df.format(date);
		
		AllInOne all = new AllInOne("");
		
		AioCheckOutALL obj = new AioCheckOutALL();
		obj.setMerchantTradeNo(uuId);
		obj.setMerchantTradeDate(dateString);
		obj.setTotalAmount((String) wallet.get("change"));
		obj.setTradeDesc("Carbon-錢包儲值");
		obj.setItemName((String) wallet.get("desc"));
	    // 交易結果回傳網址，只接受 https 開頭的網站，可以使用 ngrok	
        obj.setReturnURL("http://127.0.0.1:4040");
		obj.setNeedExtraPaidInfo("N");
        // 商店轉跳網址 (Optional)
        obj.setClientBackURL("http://localhost:8080/carbon/profile/wallet/status?id="+ uuId +"&memberId="+ wallet.get("memberId") +"&desc="+ wallet.get("desc"));
		String form = all.aioCheckOut(obj, null);
		
		return form;
	}
	
	public boolean postQueryTradeInfo(Map<String, Object> walletForm){
		AllInOne all = new AllInOne("");
		QueryTradeInfoObj obj = new QueryTradeInfoObj();
		obj.setMerchantTradeNo((String) walletForm.get("id"));
		String tradeInfo = all.queryTradeInfo(obj);
		System.out.println(tradeInfo);
		
		int tradeStatus = 0 ;
		Float change = null;
		String desc = null;
		
		
		String[] infos = tradeInfo.split("&");
		for(String info : infos) {
			String[] result = info.split("=");
			if(result.length == 2 && result[0].equals("TradeStatus")) {
				tradeStatus = Integer.parseInt(result[1]);
			}
			if(result.length == 2 && result[0].equals("TradeAmt")) {
				try {
					change = Float.parseFloat(result[1]);
				} catch (NumberFormatException e) {
			        e.printStackTrace();
			    }
			}
			if(result.length == 2 && result[0].equals("ItemName")) {
				desc = result[1];
			}
		}
		
		if(tradeStatus == 1) {
			Wallet wallet = new Wallet();
			wallet.setMemberId(Integer.parseInt((String) walletForm.get("memberId")));
			wallet.setTradeNo((String) walletForm.get("id"));
			wallet.setChange(change);
			wallet.setChangeDesc(desc);
			insert(wallet);
			return true;
		} else {
			return false;
		}
	}
	
//	======================= 轉換 DTO 和 Entity =======================
	public List<WalletDTO> convertToDTOList(List<Wallet> wallets) {
		List<WalletDTO> walletDTOs = new ArrayList<>();
		if(wallets != null) {
			for(Wallet wallet : wallets) {
				WalletDTO walletDTO = new WalletDTO();
				if(wallet.getId() != null) walletDTO.setId(wallet.getId());
				if(wallet.getTradeNo() != null) walletDTO.setTradeNo(wallet.getTradeNo());
				if(wallet.getMemberId() != null) walletDTO.setMemberId(wallet.getMemberId());
				if(wallet.getChange() != null) walletDTO.setChange(wallet.getChange());
				if(wallet.getBalance() != null) walletDTO.setBalance(wallet.getBalance());
				if(wallet.getChangeDesc() != null) walletDTO.setChangeDesc(wallet.getChangeDesc());
				if(wallet.getCreateTime() != null) walletDTO.setCreateTime(wallet.getCreateTime());
				if(wallet.getMember() != null) walletDTO.setMember(wallet.getMember());
				walletDTOs.add(walletDTO);
			}
			return walletDTOs;
		}
		return null;
	}
	
	public WalletDTO convertToDTO(Wallet wallet) {
		WalletDTO walletDTO = new WalletDTO();
		if(wallet != null) {
			if(wallet.getId() != null) walletDTO.setId(wallet.getId());
			if(wallet.getTradeNo() != null) walletDTO.setTradeNo(wallet.getTradeNo());
			if(wallet.getMemberId() != null) walletDTO.setMemberId(wallet.getMemberId());
			if(wallet.getChange() != null) walletDTO.setChange(wallet.getChange());
			if(wallet.getBalance() != null) walletDTO.setBalance(wallet.getBalance());
			if(wallet.getChangeDesc() != null) walletDTO.setChangeDesc(wallet.getChangeDesc());
			if(wallet.getCreateTime() != null) walletDTO.setCreateTime(wallet.getCreateTime());
			if(wallet.getMember() != null) walletDTO.setMember(wallet.getMember());
			return walletDTO;
		}
		return null;
	}
}
