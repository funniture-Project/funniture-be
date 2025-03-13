package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.exception.DuplicatedMemberEmailException;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.point.entity.PointEntity;
import com.ohgiraffers.funniture.point.model.dao.PointRepository;
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
    private final PointRepository pointRepository;

    public AuthService(PasswordEncoder passwordEncoder
                    , MemberRepository memberRepository
                    , ModelMapper modelMapper
                    , PointRepository pointRepository){
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
        this.pointRepository = pointRepository;
    }

    @Transactional
    public MemberDTO authSignupService(MemberDTO memberDTO) {

        memberDTO.setMemberRole("USER");
        memberDTO.setSignupDate(LocalDateTime.now());
        memberDTO.setIsConsulting(false);

        // 이메일 중복 유효성 검사
        if (memberRepository.existsByEmail(memberDTO.getEmail())) {
            throw new DuplicatedMemberEmailException("이메일이 중복됩니다.");
        }

        // DTO에 담아온 값 엔티티화.
        MemberEntity registMember = modelMapper.map(memberDTO , MemberEntity.class);

        // 패스워드 인코딩해서 registMember에 담음
        registMember = registMember.password(passwordEncoder.encode(registMember.getPassword())).create();

        // 인코딩한 패스워드와 회원가입 정보 저장하여 result에 담기
        MemberEntity result = memberRepository.save(registMember);

        // 인서트 되고 포인트 100000점 추가해주기
        addPointBySignup(result.getMemberId());

        return modelMapper.map(result , MemberDTO.class);
    }

    public void addPointBySignup (String memberId) {

        // 초기 포인트 값 설정
        int signupBonus = 100000;

        // 포인트 엔티티 생성
        PointEntity pointEntity = PointEntity.builder()
                .memberId(memberId)
                .usedPoint(0)
                .addPoint(signupBonus)
                .currentPoint(signupBonus) // 초기 보유 포인트
                .build();

        // 포인트 저장
        pointRepository.save(pointEntity);

    }

    public String getMaxMember() {

        String maxNo = memberRepository.maxMemberNo();
        return maxNo;
    }

    // 회원 가입 시 중복 이메일 있는지 검증 서비스
    public Boolean validationDuplicateEmail(String email) {

       Boolean result = memberRepository.existsByEmail(email);

       return result;
    }
}
