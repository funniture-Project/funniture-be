package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberDTO getMemberList(String memberId) {

        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        System.out.println("✅ 로그인한 정보 찾아온 엔티티 값 : " + memberEntity);

        return modelMapper.map(memberEntity , MemberDTO.class);
    }

    // 비밀번호 변경 전, email을 이용하여 id 찾아오는 로직
    public MemberEntity findByEmail(String email) {
        System.out.println("비밀번호 변경 전, 서비스로 email 넘어 왔나 = " + email);

        return memberRepository.findByEmail(email);

    }

    // 로그인 페이지에서 비밀번호 변경하는 로직
    @Transactional
    public MemberEntity changePassword(MemberEntity memberEntity ,String newPassword) {

        System.out.println("컨트롤러에서 이메일에 해당하는 데이터 찾아온 기존 회원 정보 = " + memberEntity);

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(newPassword);
        System.out.println("인코딩된 비밀번호 = " + encodedPassword);

        // 기존 회원 엔티티에 새로운 비밀번호 설정
        memberEntity.setPassword(encodedPassword);

        // DB에 저장
        MemberEntity result = memberRepository.save(memberEntity);
        System.out.println("비밀번호 변경 완료. : " + result);
        return result;
    }
}
