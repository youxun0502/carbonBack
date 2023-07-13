package com.liu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evan.dao.GameTypeRepository;
import com.evan.model.GameType;
import com.liu.config.RandomConfig;
import com.liu.model.Coupon;
import com.liu.model.CouponRepository;

@Service
public class CouponService {

	@Autowired
	CouponRepository couponRepository;
	
	@Autowired
	RandomConfig randomConfig;
	
	@Autowired
	GameTypeRepository gameTypeRepository;
	
	public Integer getTotalWeight() {
		return couponRepository.getTotalWeight();
	}
	
	
	public Map<Integer,Integer> getCouponRandom(){
		if (randomConfig.getCouponRandom() == null) {	
				System.out.println("------new CouponRandom---------");
			return createNewRamdomForCoupon();
				
		}
		System.out.println("------old CouponRandom---------");
		return randomConfig.getCouponRandom();
	}
	
	public Map<Integer, Float> getCouponRamdomForManagement(){
		Integer totalWeight = getTotalWeight();
		List<Coupon> coupons = couponRepository.findCouponWhereStatusNotEqualOneOrderByCouponId();
		Map<Integer,Float> newRamdom = new HashMap<>();
		for (Coupon coupon : coupons) {
			Integer couponId = coupon.getCouponId();
			Integer weight = coupon.getWeight();
			float random = (float)weight/totalWeight;
			newRamdom.put(couponId, random);
		}
		return newRamdom;
	}


	private Map<Integer, Integer> createNewRamdomForCoupon() {
		Integer totalWeight = getTotalWeight();
		List<Coupon> coupons = couponRepository.findCouponWhereStatusNotEqualOneOrderByCouponId();
		Map<Integer,Integer> newRamdom = new HashMap<>();
		Integer tempMaxNum = 0;
		for (Coupon coupon : coupons) {
			Integer couponId = coupon.getCouponId();
			Integer weight = coupon.getWeight();
			float random = (float)weight/totalWeight; //機綠
			Integer maxNum = randomConfig.getTotalWeight();  //10000
			Integer couponRandomNum = (int) (tempMaxNum+maxNum*random); //0+10000*random=3333 //3333+3333=6666 6666+3333
			tempMaxNum = couponRandomNum;//0+3333 3333+6666
			System.out.println(couponId);
			System.out.println(weight);
			System.out.println(random);
			System.out.println(maxNum);		
			System.out.println(tempMaxNum);
			System.out.println(couponRandomNum);
			newRamdom.put(couponId, couponRandomNum);
		}
		randomConfig.setCouponRandom(newRamdom);
		return newRamdom;
	}


	public Coupon getCouponByRandom(int random) {
		System.out.println(random);
		System.out.println("-----------------------------------");
		Map<Integer,Integer> couponRandom = getCouponRandom();
		List<Coupon> coupons = couponRepository.findCouponWhereStatusNotEqualOneOrderByCouponId();
		int temp = 0;
		System.out.println(coupons.size());
		System.out.println("already");
		for (Coupon coupon : coupons) {
			System.out.println(couponRandom.get(coupon.getCouponId()));
		
			if(temp < random && random <= couponRandom.get(coupon.getCouponId())) {
				System.out.println(coupon.getCouponId());
				return coupon;
			}
		}
		System.out.println("null");
		return null;	
	}
	
	public List<Coupon> findCouponOrderByCouponId(){
		return couponRepository.findCouponOrderByCouponId();
	}
	
	public List<Coupon> findCouponWhereStatusNotEqualOneOrderByCouponId(){
		return couponRepository.findCouponWhereStatusNotEqualOneOrderByCouponId();
	}
	
	public Boolean updateCouponStatus(Integer id, Integer status){
		Coupon oldCoupon = couponRepository.getReferenceById(id);
		oldCoupon.setStatus(status);
		couponRepository.save(oldCoupon);
		createNewRamdomForCoupon();
		return true;
	}
	
	public Boolean insertCoupon(Coupon coupon) {
		
		if(coupon.getTypeId()!=null) {
		Integer typeId = coupon.getTypeId();
		GameType type = gameTypeRepository.getReferenceById(typeId);
		coupon.setGameType(type);
		}

		
		
		couponRepository.save(coupon);
		createNewRamdomForCoupon();
		return true;
	}
	
}
