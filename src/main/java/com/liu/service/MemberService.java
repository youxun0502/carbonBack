package com.liu.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.liu.model.Level;
import com.liu.model.LevelRepository;
import com.liu.model.Member;
import com.liu.model.MemberRepository;

import jakarta.transaction.Transactional;

@Service
public class MemberService {

	@Autowired
	MemberRepository mRepository;

	@Autowired
	LevelRepository levelRepository;

	public List<Member> findAll() {
		return mRepository.findAll();
	}

	public List<Member> findByName(String memberName) {
		return mRepository.findMemberByName(memberName);
	}

	//用在找update的資料
	public Member findById(Integer id) {
		Optional<Member> optional = mRepository.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Transactional
	public boolean update(Member member) {
		Optional<Member> optional = mRepository.findById(member.getId());

		if (optional.isPresent()) {
			Member updateMember = optional.get();
			updateMember.setMemberPwd(member.getMemberPwd());
			updateMember.setMemberName(member.getMemberName());
			updateMember.setBirthday(member.getBirthday());
			updateMember.setGender(member.getGender());
			updateMember.setPhone(member.getPhone());
			updateMember.setLevelId(member.getLevelId());
			updateMember.setAccount(member.getAccount());
			return true;
		}
		return false;
	}

	public void deleteById(Integer id) {
		mRepository.deleteById(id);
	}

	public Member isMember(String email, String memberPwd) {
		return mRepository.isMember(email, memberPwd);
	}

	public boolean emailAlreadyRegistered(String email) {
		Member member = mRepository.emailAlreadyRegistered(email);
		if (member != null) {
			return true;
		}
		return false;
	}

	public boolean phoneAlreadyRegistered(String phone) {
		Member member = mRepository.phoneAlreadyRegistered(phone);
		if (member != null) {
			return true;
		}
		return false;
	}

	public boolean insert(Member member) {
		// aJax emailAlreadyRegistered() , phoneAlreadyRegistered() 先判斷是否註冊過了

		Set<Member> members = new HashSet<>();
		members.add(member);

		Optional<Level> optional = levelRepository.findById(1);
		if (optional.isPresent()) {
			Level level = optional.get();
			level.setMember(members);
			member.setLevel(level);
			mRepository.save(member);
			levelRepository.save(level);
			return true;
		}
		return false;

	}

}
