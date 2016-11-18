package com.rightnow.service;

import com.rightnow.details.LoginUserDetails;
import com.rightnow.domain.Member;
import com.rightnow.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by HeemangHan on 2016. 11. 9..
 */

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member getMember(String id) {
        return memberRepository.findOneById(id);
    }

    @Transactional
    public boolean create(Member joinMember) {
        if(memberRepository.findOneById(joinMember.getId()) != null)
            return false;

        Member member = new Member();
        member.setId(joinMember.getId());
        member.setPassword(new BCryptPasswordEncoder().encode(joinMember.getPassword()));
        member.setName(joinMember.getName());
        member.setTel(joinMember.getTel());
        member.setRole("NORMAL");
        memberRepository.save(member);

        return true;
    }

    public String getMemberId() {
        String memberName = null;

        try {
            LoginUserDetails userDetails = (LoginUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            int memberIdx = userDetails.getIdx();

            Member member = memberRepository.findOneByIdx(memberIdx);
            memberName = member.getName();
        } catch (Exception e) {
            memberName = null;
        }

        return memberName;
    }
}
