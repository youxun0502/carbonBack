package com.liu.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.liu.dto.MemberDto;
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

	@Autowired
	private PasswordEncoder pwdEncoder;

	public List<Member> findAll() {
		return mRepository.findAll();
	}

	public List<Member> findByName(String memberName) {
		return mRepository.findMemberByName(memberName);
	}

	// 用在找update的資料
	public Member findById(Integer id) {
		Optional<Member> optional = mRepository.findById(id);

		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Transactional
	public boolean updateStatus(Member member) {
		Optional<Member> optional = mRepository.findById(member.getId());

		if (optional.isPresent()) {
			Member updateMember = optional.get();
			updateMember.setMemberPwd(member.getMemberPwd());
			updateMember.setMemberName(member.getMemberName());
			updateMember.setBirthday(member.getBirthday());
			updateMember.setGender(member.getGender());
			updateMember.setPhone(member.getPhone());
			if (member.getLevelId() == 100) {
				Optional<Level> level = levelRepository.findById(100);
				if (level.isPresent()) {
					updateMember.setLevel(level.get());
				}

			} else {
				Optional<Level> level = levelRepository.findById(1);
				if (level.isPresent()) {
					updateMember.setLevel(level.get());
				}
			}
			updateMember.setAccount(member.getAccount());
			return true;
		}
		return false;
	}
	
	@Transactional
	public boolean update(MemberDto member) {
		Optional<Member> optional = mRepository.findById(Integer.parseInt(member.getInnerId()) );

		if (optional.isPresent()) {
			Member updateMember = optional.get();
			if(!member.getPwd().equals(optional.get().getMemberPwd())) {
				updateMember.setMemberPwd(pwdEncoder.encode(member.getPwd()));
			}
			updateMember.setMemberName(member.getName());
			updateMember.setBirthday(member.getBirthday());
			updateMember.setGender(member.getGender());
			updateMember.setPhone(member.getPhone());
			if (member.getLevel() == 100) {
				Optional<Level> level = levelRepository.findById(100);
				if (level.isPresent()) {
					updateMember.setLevel(level.get());
				}

			} else {
				Optional<Level> level = levelRepository.findById(1);
				if (level.isPresent()) {
					updateMember.setLevel(level.get());
				}
			}
			updateMember.setAccount(member.getAccount());
			return true;
		}
		return false;
	}

	@Transactional
	public boolean deleteById(Integer id) {
		Member member = mRepository.getReferenceById(id);
		member.setStatus(2);
		return true;
	}
	
	@Transactional
	public boolean restoreById(Integer id) {
		Member member = mRepository.getReferenceById(id);
		member.setStatus(1);
		return true;
	}

	public Member isMember(String email, String memberPwd) {
		Member member = mRepository.findMemberByEmail(email);

		if (member != null) {
			if (pwdEncoder.matches(memberPwd, member.getMemberPwd())) {
				return member;
			} else {
				return null;
			}
		}
		return null;
	}

	// 註冊驗證email
	public boolean emailAlreadyRegistered(String email) {
		Member member = mRepository.findMemberByEmail(email);
		if (member != null) {
			return true;
		}
		return false;
	}
	public Member findByEmail(String email) {
		return mRepository.findMemberByEmail(email);
		
	}

	// 註冊驗證phone
	public boolean phoneAlreadyRegistered(String phone) {
		Member member = mRepository.findMemberByPhone(phone);
		if (member != null) {
			return true;
		}
		return false;
	}

	public boolean insert(Member member) {
		// aJax emailAlreadyRegistered() , phoneAlreadyRegistered() 先判斷是否註冊過了

		Set<Member> members = new HashSet<>();
		member.setMemberPwd(pwdEncoder.encode(member.getMemberPwd()));
		if(member.getPhone()== null) {
			member.setStatus(1);
		}else {
			member.setStatus(3);
		}

		
		members.add(member);

		Optional<Level> optional = levelRepository.findById(1);
		if (optional.isPresent()) {
			Level level = optional.get();
			// level.setMember(members);/*level已經改變了，如果下方不寫levelRepository.save(level);
			// ，那就要在方法前加上@Transactional*/
			member.setLevel(level);
			mRepository.save(member);
			// levelRepository.save(level);
			return true;
		}
		return false;

	}
	
	public  List<Object[]> findRegistrationMonth(String year){
		return mRepository.findRegistrationMonth(year);
	}
	
	public List<String> findAllRegistrationYear(){
		return mRepository.findAllRegistrationYear();
	}
	
	public List<Object[]> findRegistrationGender(String year){
		return mRepository.findRegistrationGender(year);
	}
	
	@Transactional
	public void changePwdForForgetPwd(String email, String pwd) {
		Member member = mRepository.findMemberByEmail(email);
		member.setMemberPwd(pwdEncoder.encode(pwd));
		mRepository.save(member);
	}

	//-------修改頭像，外框，背景
	@Transactional
	public boolean updateAvatar(Integer id,Integer avatar,Integer frame,Integer background) {
		Optional<Member> optional = mRepository.findById(id);

		if (optional.isPresent()) {
			Member updateMember = optional.get();
			updateMember.setUseAvatar(avatar);
			updateMember.setUseFrame(frame);
			updateMember.setUseBackground(background);			
			return true;
		}
		return false;
	}
}
