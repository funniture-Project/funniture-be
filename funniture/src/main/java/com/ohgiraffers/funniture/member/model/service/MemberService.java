package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dao.OwnerRepository;
import com.ohgiraffers.funniture.member.model.dto.AppOwnerInfoDTO;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import jakarta.validation.Valid;
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
    private final OwnerRepository ownerRepository;
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

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
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

    @Transactional
    public void updateMemberImage(MemberDTO memberDTO) {
        System.out.println("서비스의 memberDTO = " + memberDTO);

        // 데이터베이스에서 기존 회원 정보 조회
        MemberEntity existingMember = memberRepository.findById(memberDTO.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다: " + memberDTO.getMemberId()));

        // 새로운 이미지 링크만 업데이트
        if (memberDTO.getImageLink() != null) {
            existingMember.setImageLink(memberDTO.getImageLink());
        }

        // 변경 사항 저장 (변경 감지 또는 save 호출)
        memberRepository.save(existingMember);
    }

    @Transactional
    public MemberDTO withdrawService(String memberId) {

        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 회원이 존재하지 않습니다."));

        memberEntity.setMemberRole("LIMIT");

        MemberDTO result = modelMapper.map(memberEntity , MemberDTO.class);

        return result;

    }

    @Transactional
    public void registerOwner(@Valid AppOwnerInfoDTO appOwnerInfoDTO) {
        // 1. tbl_member에서 회원 정보 조회
        MemberEntity memberEntity = memberRepository.findByMemberId(appOwnerInfoDTO.getMemberId());

        System.out.println("서비스 제공자 전환 신청 memberEntity  = " + memberEntity);
        // 2. owner 테이블에 데이터가 이미 존재하는지 확인
        OwnerInfoEntity ownerInfoEntity;
        if (ownerRepository.existsByMemberId(memberEntity.getMemberId())) {
            // 기존 엔티티 삭제
            OwnerInfoEntity existingEntity = ownerRepository.findByMemberId(memberEntity.getMemberId())
                    .orElseThrow(() -> new IllegalStateException("데이터가 존재해야 하지만 찾을 수 없습니다."));
            ownerRepository.delete(existingEntity);
        }

        appOwnerInfoDTO.setIsRejected(0);

        // 3. 엔티티에 데이터 설정
        ownerInfoEntity = new OwnerInfoEntity();
        ownerInfoEntity.setMemberId(memberEntity.getMemberId());
        ownerInfoEntity.setStoreNo(appOwnerInfoDTO.getStoreNo());
        ownerInfoEntity.setStoreName(appOwnerInfoDTO.getStoreName());
        ownerInfoEntity.setBank(appOwnerInfoDTO.getBank());
        ownerInfoEntity.setAccount(appOwnerInfoDTO.getAccount());
        ownerInfoEntity.setStoreAddress(appOwnerInfoDTO.getStoreAddress());
        ownerInfoEntity.setStorePhone(appOwnerInfoDTO.getStorePhone());
        ownerInfoEntity.setIsRejected(appOwnerInfoDTO.getIsRejected());
        ownerInfoEntity.setAttechmentLink(appOwnerInfoDTO.getAttechmentLink());
        ownerInfoEntity.setStoreImage(appOwnerInfoDTO.getStoreImage());

        ownerRepository.save(ownerInfoEntity);


        // 4. 데이터베이스 저장
        ownerRepository.save(ownerInfoEntity);
        System.out.println("서비스에서 잘 저장 됐는지 = ");
    }

}
