package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public AuthService(PasswordEncoder passwordEncoder
                    , MemberRepository memberRepository
                    , ModelMapper modelMapper){
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public MemberDTO authSignupService(MemberDTO memberDTO) {

        System.out.println("서비스 : 컨트롤러에서 온 memberDTO = " + memberDTO);

        memberDTO.setMemberRole("USER");
        memberDTO.setSignupDate(LocalDateTime.now());

        // DTO에 담아온 값 엔티티화.
        MemberEntity registMember = modelMapper.map(memberDTO , MemberEntity.class);
        System.out.println("엔티티화 한 값 registMember = " + registMember);

        // 패스워드 인코딩해서 registMember에 담음
        registMember = registMember.password(passwordEncoder.encode(registMember.getPassword())).create();
        System.out.println("패스워드 인코딩 한 registMember = " + registMember);

        // 인코딩한 패스워드와 회원가입 정보 저장하여 result에 담기
        MemberEntity result = memberRepository.save(registMember);
        System.out.println("인코딩한 정보 result = " + result);

        return modelMapper.map(result , MemberDTO.class);
    }


    public String getMaxMember() {

        String maxNo = memberRepository.maxMemberNo();
        System.out.println("maxNo 잘 받아 오는지 = " + maxNo);
        return maxNo;
    }
}
