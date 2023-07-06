package com.evan.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evan.dao.GameRepository;
import com.evan.dto.CartDTO;
import com.evan.model.Game;
import com.evan.utils.ConvertToDTO;
import com.liu.model.Member;
import com.liu.model.MemberRepository;

@Service
public class CartService {

	@Autowired
	private MemberRepository mRepos;
	@Autowired
	private GameRepository gRepos;

	@Autowired
	private ConvertToDTO cdDTO;

	public List<CartDTO> getMemberCart(Integer memberId) {

		Optional<Member> members = mRepos.findById(memberId);

		if (members.isPresent()) {
			Member member = members.get();
			return cdDTO.ouptCartDTOs(member);
		}
		return null;
	}

	public List<CartDTO> addMemberCart(Map<String, Object> formData) {
		Object memberId = formData.get("memberId");
		Member member = mRepos.findById((Integer.parseInt((String) memberId))).orElse(null);
		//購物車有幾個商品
		int i =(formData.size()-1)/4;
			// 獲取 cartItems 的值
			
			if (i != 0) {
				for (int j = 0; j < i; j++ ) {
					// 從 item 中獲取遊戲相關資訊，例如 gameId, gameName, price, photo
					Integer gameId = Integer.parseInt((String) formData.get("cartItems["+j+"][gameId]"));
					System.out.println(gameId);
					Game game = gRepos.findById(gameId).orElse(null);
					if (member != null && game != null) {
						member.getGames().add(game);
					}
				}
			}
			mRepos.save(member);
			return cdDTO.ouptCartDTOs(member);
	}
	
	public void addOne(Map<String, Object> formData) {
		Object memberId = formData.get("memberId");
		Member member = mRepos.findById((Integer.parseInt((String) memberId))).orElse(null);
		
		Integer gameId = Integer.parseInt((String) formData.get("gameId"));
		Game game = gRepos.findById(gameId).orElse(null);
		member.getGames().add(game);
		mRepos.save(member);
	}
	
	public void deleteOne(Map<String, Object> formData) {
		Object memberId = formData.get("memberId");
		Member member = mRepos.findById((Integer.parseInt((String) memberId))).orElse(null);
		
		Integer gameId = Integer.parseInt((String) formData.get("gameId"));
		Game game = gRepos.findById(gameId).orElse(null);
		member.getGames().remove(game);
		mRepos.save(member);
	}
	public void deleteAll(Map<String, Object> formData) {
		Object memberId = formData.get("memberId");
		Member member = mRepos.findById((Integer.parseInt((String) memberId))).orElse(null);
		
		member.getGames().clear();
		mRepos.save(member);
	}
	
	
	
}
