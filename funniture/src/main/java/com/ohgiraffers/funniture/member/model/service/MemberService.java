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

    public boolean comparePassword(String memberId, String password) {

        System.out.println("서비스에 아이디 비번 잘 넘어 왔는지 memberId = " + memberId + password);

        // 받아온 id 값을 가지고 정보 찾아오기
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
        System.out.println("memberId로 memberEntity 잘 찾아왔는지 = " + memberEntity);

        // 이미 저장돼있던 인코딩 패스워드
        String encodedPassword = memberEntity.getPassword();

        // passwordEncoder.matches는 사용자 입력 패스워드(암호화x)와 저장된 암호화 패스워드를 비교해줌
        if (passwordEncoder.matches(password, encodedPassword)){
            System.out.println(" 비밀번호 일치 ");
            return true;
        } else{
            System.out.println("비밀번호 불일치");
            return false;
        }
    }

    // 마이페이지 비번 변경 전 해당 id에 해당하는 엔티티 찾아오는 로직
    public MemberEntity findByMemberId(String memberId) {
        System.out.println("비밀번호 변경 전, 서비스로 memberId 넘어 왔나 = " + memberId);

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
        System.out.println("서비스에서 아이디에 해당하는 memberEntity 잘 찾았나 = " + memberEntity);
        return memberEntity;

    }

    // 마이페이지에서 전화번호 변경 로직
    @Transactional
    public MemberEntity changePhoneNumber(String memberId, String phoneNumber) {
        System.out.println("서비스 memberId = " + memberId);
        System.out.println("서비스 phoneNumber = " + phoneNumber);

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
        memberEntity.setPhoneNumber(phoneNumber);

        MemberEntity result =  memberRepository.save(memberEntity);
        return result;
    }

    public MemberEntity changePasswordByMypage(MemberEntity memberEntity, String newPassword) {
        System.out.println("서비스 memberEntity = " + memberEntity);
        System.out.println("서비스 newPassword = " + newPassword);

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

    public MemberEntity changeAddressByMypage(MemberEntity memberEntity, String newAddress) {
        System.out.println("서비스 memberEntity = " + memberEntity);
        System.out.println("서비스 newAddress = " + newAddress);

        // 기존 회원 엔티티에 새로운 비밀번호 설정
        memberEntity.setAddress(newAddress);

        // DB에 저장
        MemberEntity result = memberRepository.save(memberEntity);
        System.out.println("주소 변경 완료. : " + result);
        return result;

    }
}
