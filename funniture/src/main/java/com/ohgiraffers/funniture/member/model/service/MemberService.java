package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public MemberDTO getMemberList(String memberId) {

        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        System.out.println("✅ 로그인한 정보 찾아온 엔티티 값 : " + memberEntity);

        return modelMapper.map(memberEntity , MemberDTO.class);
    }
}
