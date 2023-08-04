package com.ni.aop;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.liu.service.MemberService;
import com.ni.dto.ItemLogDTO;
import com.ni.dto.ItemOrderDTO;
import com.ni.model.ItemOrder;
import com.ni.model.Wallet;
import com.ni.service.ItemLogService;
import com.ni.service.WalletService;

@Component
@Aspect
public class itemOrderAspect {

	@Autowired
	private ItemLogService logService;
	@Autowired
	private MemberService mService;
	@Autowired
	private WalletService walletService;
	
	
	
	@Pointcut("execution(* com.ni.service.ItemOrderService.insert(..)) && args(itemOrder)")
	public void pointCut(ItemOrderDTO itemOrder) {
		
	}
	
//	@Before(value = "pointCut()")
//	public void before(JoinPoint joinPoint) {
//		System.out.println("================================== 方法執行前 ==================================");
//	}
	
	@AfterReturning(pointcut = "pointCut(itemOrder)", returning = "result")
	public void after(ItemOrderDTO itemOrder, ItemOrder result) {
		System.out.println("================================== 方法執行後 ==================================");
		System.out.println("================================== 新增log ==================================");
		insertItemLog(result);
		insertWallet(result);
		System.out.println("================================== 新增結束 ==================================");
		
	}
	
	private void insertItemLog(ItemOrder orderInfo) {
		ItemLogDTO logDTO = new ItemLogDTO();
		logDTO.setOrdId(orderInfo.getOrdId());
		logDTO.setItemId(orderInfo.getItemId());
		if(orderInfo.getBuyer() != null) {
			logDTO.setMemberId(orderInfo.getBuyer());
			logDTO.setQuantity(orderInfo.getQuantity());
		} else {
			logDTO.setMemberId(orderInfo.getSeller());
			logDTO.setQuantity(Integer.parseInt("-" + orderInfo.getQuantity()));
		}
		logService.insert(logDTO);
	}
	
	private void insertWallet(ItemOrder orderInfo) {
		if(orderInfo.getBuyer() != null) {
			Wallet buyerWallet = new Wallet();
			buyerWallet.setMemberId(orderInfo.getBuyer());
			buyerWallet.setChange(Float.parseFloat("-" + orderInfo.getPrice()));
			buyerWallet.setChangeDesc("買入 道具編號: "+ orderInfo.getItemId() +" 的道具");
			walletService.insert(buyerWallet);
			Wallet sellerWallet = new Wallet();
			sellerWallet.setMemberId(orderInfo.getSeller());
			sellerWallet.setChange(orderInfo.getPrice());
			sellerWallet.setChangeDesc("賣出 道具編號: "+ orderInfo.getItemId() +" 的道具");
			walletService.insert(sellerWallet);
		}
	}
}
