package com.ni.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.evan.model.Game;
import com.liu.model.Member;
import com.liu.service.GmailService;
import com.liu.service.MemberService;
import com.ni.dto.ItemOrderDTO;
import com.ni.dto.WalletDTO;
import com.ni.model.GameItem;
import com.ni.model.ItemOrder;
import com.ni.service.GameItemService;
import com.ni.service.ItemOrderService;
import com.ni.service.WalletService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;

@Controller
public class ItemOrderController {

	@Autowired
	private ItemOrderService orderService;
	@Autowired
	private GameItemService itemService;
	@Autowired
	private GmailService gService;
	@Autowired
	private MemberService mService;
	@Autowired
	private WalletService walletService;
	
	@GetMapping("/gameitem/allOrder")
	public String getAllOrder(Model m) {
		List<ItemOrderDTO> orders = orderService.findAll();
		m.addAttribute("orders", orders);
		return "ni/orderDataTable";
	}
	@ResponseBody
	@GetMapping("/gameitem/api/allOrder")
	public List<ItemOrderDTO> getAllOrderAjax(Model m) {
		return orderService.findAll();
	}
	
	@ResponseBody
	@PutMapping("/gameitem/orderUpdate")
	public boolean update(@RequestBody ItemOrderDTO order) {
		ItemOrder result = orderService.updateStatusById(order.getOrdId(), order.getStatus());
		return result != null;
	}

	
//	----------------------------- gameItemMarket -----------------------------
	@GetMapping("/market")
	public String marketList(Model m) {
		m.addAttribute("orders", orderService.findMinPrice());
		List<GameItem> gameItems = itemService.findAll();
		List<Game> games = new ArrayList<>();
		for(GameItem gameItem : gameItems) {
			Game game = gameItem.getGame();
			if(! games.contains(game)) {
				games.add(gameItem.getGame());
			}
		}
		m.addAttribute("games", games);
		return "ni/itemMarketList-gg";
	}
	
	@ResponseBody
	@GetMapping("/market/page")
	public Page<ItemOrderDTO> marketListPage(@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		List<GameItem> gameItems = itemService.findAll();
		List<ItemOrderDTO> orderList = new ArrayList<>();
		for (GameItem gameItem : gameItems) {
			ItemOrderDTO orderDTO = new ItemOrderDTO();
			orderDTO.setItemId(gameItem.getItemId());
			orderDTO.setGameItem(gameItem);
			orderList.add(orderDTO);
		}
		
		List<Map<String, Object>> priceList = orderService.findMinPrice();
		for(ItemOrderDTO order : orderList) {
			for (Map<String, Object> price : priceList) {
				if(Integer.parseInt(price.get("itemId").toString()) == order.getItemId()) {
					order.setItemId(Integer.parseInt(price.get("itemId").toString()));
					order.setMinPrice(Float.parseFloat(price.get("minPrice").toString()));
				}
			}
		}
		Pageable pgb = PageRequest.of(pageNumber - 1, 9);
		
		int fromIndex = pgb.getPageSize() * pgb.getPageNumber();
        int toIndex = pgb.getPageSize() * (pgb.getPageNumber() + 1);
        if( toIndex > orderList.size() ) toIndex = orderList.size();
        //类似取mysql数据库的情况，因为数据取数本身支持分页，所以list的数据每次都取当前页就可以，但是需要先要计算所有记录数，然后传入totalElements变量
        List<ItemOrderDTO> subOrderDTOs = orderList.subList(fromIndex, toIndex);
		
	    // 将orderList转换为Page对象
        Page<ItemOrderDTO> page = new PageImpl<>(subOrderDTOs, pgb, orderList.size());
	    System.out.println(orderList.size());
		
		return page;
	}
	
	@ResponseBody
	@GetMapping("/market/page/find")
	public Page<ItemOrderDTO> marketListFindByName(@RequestParam("itemName") String itemName, @RequestParam("gameId") Integer gameId, @RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		List<GameItem> gameItems;
		if(itemName != null && gameId != 0) {
			gameItems = itemService.findByNameAndGame(gameId, itemName);
		} else {
 			gameItems = itemService.findByNameOrGame(gameId, itemName);
		}
		List<ItemOrderDTO> orderList = new ArrayList<>();
		for (GameItem gameItem : gameItems) {
			ItemOrderDTO orderDTO = new ItemOrderDTO();
			orderDTO.setItemId(gameItem.getItemId());
			orderDTO.setGameItem(gameItem);
			orderList.add(orderDTO);
		}
		
		List<Map<String, Object>> priceList = orderService.findMinPrice();
		for(ItemOrderDTO order : orderList) {
			for (Map<String, Object> price : priceList) {
				if(Integer.parseInt(price.get("itemId").toString()) == order.getItemId()) {
					order.setItemId(Integer.parseInt(price.get("itemId").toString()));
					order.setMinPrice(Float.parseFloat(price.get("minPrice").toString()));
				}
			}
		}
		Pageable pgb = PageRequest.of(pageNumber - 1, 9);
		
		int fromIndex = pgb.getPageSize() * pgb.getPageNumber();
		int toIndex = pgb.getPageSize() * (pgb.getPageNumber() + 1);
		if( toIndex > orderList.size() ) toIndex = orderList.size();
		//类似取mysql数据库的情况，因为数据取数本身支持分页，所以list的数据每次都取当前页就可以，但是需要先要计算所有记录数，然后传入totalElements变量
		List<ItemOrderDTO> subOrderDTOs = orderList.subList(fromIndex, toIndex);
		
		// 将orderList转换为Page对象
		Page<ItemOrderDTO> page = new PageImpl<>(subOrderDTOs, pgb, orderList.size());
		System.out.println(orderList.size());
		
		return page;
	}
	
	@GetMapping("/market/{gameId}/{itemId}/{itemName}")
	public String marketItem(@PathVariable Integer gameId, @PathVariable String itemName, @PathVariable Integer itemId, Model m) {
		List<ItemOrderDTO> result = orderService.findSellItemList(gameId, itemName);
		m.addAttribute("item", itemService.findById(itemId));
		m.addAttribute("orders", result);
		return "ni/itemMarketPage-gg";
	}
	
	@ResponseBody
	@GetMapping("/market/orderLIst")
	public List<ItemOrderDTO> orderList(@PathVariable Integer gameId, @PathVariable String itemName, Model m) {
		return orderService.findSellItemList(gameId, itemName);
	}
	
	@ResponseBody
	@GetMapping("/market/buyOrder")
	public Page<ItemOrder> findBuyOrder(@RequestParam("memberId") Integer memberId, 
						@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		return orderService.findBuyOrder(memberId, pageNumber);
	}
	@ResponseBody
	@GetMapping("/market/saleList")
	public Page<ItemOrder> findsaleList(@RequestParam("memberId") Integer memberId, 
						@RequestParam(name = "p", defaultValue = "1") Integer pageNumber) {
		return orderService.findSaleList(memberId, pageNumber);
	}
	
	@ResponseBody
	@GetMapping("/market/buyAnItem")
	public ItemOrderDTO buyPage(@RequestParam("ordId") Integer ordId, @RequestParam("id") Integer memberId, Model m) {
		ItemOrderDTO order = orderService.findById(ordId);
		WalletDTO balance = walletService.findBalance(memberId);
		System.out.println(balance);
		if(balance != null && order.getPrice() <= balance.getBalance()) {
			order.setNeedFund(1);
		} else {
			order.setNeedFund(0);
		}
		return order;
	}
	
	@ResponseBody
	@PostMapping("/market/done")
	public ItemOrderDTO buy(@RequestBody ItemOrderDTO orderDTO) throws AddressException, MessagingException, IOException {
		ItemOrder newOrder = orderService.insert(orderDTO);
		ItemOrderDTO orderInfo = orderService.findById(newOrder.getOrdId());
		
		if(orderInfo.getBuyer() != null) {
			Member buyer = mService.findById(orderInfo.getBuyer());
			Member seller = mService.findById(orderInfo.getSeller());
			
			String buyerUrl = "http://localhost:8080/carbon/profile/" + buyer.getId() + "/inventory";
			String sellerUrl = "http://localhost:8080/carbon/profile/" + seller.getId() + "/inventory";
			gService.sendMessage(buyer.getEmail(), gService.getMyEmail(), "Carbon虛寶市集交易成功通知",
					"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + buyer.getUserId() + "您好:\n" + "\n" + 
							"感謝您此次於Carbon完成虛寶交易，點選以下連結前往個人頁面\n" + "\n" + buyerUrl
							+ "\n\n" + "Carbon lys7744110@gmail.com");
			gService.sendMessage(seller.getEmail(), gService.getMyEmail(), "Carbon虛寶市集交易成功通知",
					"此為系統發送郵件，請勿直接回覆！！！\n" + "\n" + seller.getUserId() + "您好:\n" + "\n" + 
							"感謝您此次於Carbon完成虛寶交易，點選以下連結前往個人頁面\n" + "\n" + sellerUrl
							+ "\n\n" + "Carbon lys7744110@gmail.com");
		} 
		return orderInfo;
	}
	
	@ResponseBody
	@PostMapping("/market/newOrder")
	public ItemOrderDTO insert(@RequestBody ItemOrderDTO orderDTO) {
		ItemOrder newOrder = orderService.insertOrder(orderDTO);
		return orderService.findById(newOrder.getOrdId());
	}
	
	@ResponseBody
	@PutMapping("/market/orderUpdate")
	public ItemOrder updateStatus(@RequestBody ItemOrderDTO order) {
		ItemOrder result = orderService.updateStatusById(order.getOrdId(), order.getStatus());
		return result;
	}
	
	@ResponseBody
	@GetMapping("/market/itemPrices")
	public List<ItemOrderDTO> findByItemIdAndStatus(@RequestParam("itemId") Integer itemId) {
		return orderService.findByItemIdAndStatus(itemId);
	}
	
	@ResponseBody
	@GetMapping("/market/medianPrice")
	public List<Map<String, Object>> findMedianPrice(@RequestParam("itemId") Integer itemId) {
		return orderService.findMedianPrice(itemId);
	}
	
	@ResponseBody
	@GetMapping("/market/checkBuys")
	public List<ItemOrderDTO> checkBuysPrice(@RequestParam("itemId") Integer itemId) {
		return orderService.checkBuysPrice(itemId);
	}
	
	@ResponseBody
	@GetMapping("/market/checkSales")
	public List<ItemOrderDTO> checkSalesPrice(@RequestParam("itemId") Integer itemId) {
		return orderService.checkSalesPrice(itemId);
	}
	
	
}
