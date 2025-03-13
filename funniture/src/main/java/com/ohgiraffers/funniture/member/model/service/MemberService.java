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

import java.time.LocalDateTime;
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

        return modelMapper.map(memberEntity , MemberDTO.class);
    }

    // 비밀번호 변경 전, email을 이용하여 id 찾아오는 로직
    public MemberEntity findByEmail(String email) {

        return memberRepository.findByEmail(email);

    }

    // 로그인 페이지에서 비밀번호 변경하는 로직
    @Transactional
    public MemberEntity changePassword(MemberEntity memberEntity ,String newPassword) {


        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 기존 회원 엔티티에 새로운 비밀번호 설정
        memberEntity.setPassword(encodedPassword);

        // DB에 저장
        MemberEntity result = memberRepository.save(memberEntity);
        return result;
    }

    public boolean comparePassword(String memberId, String password) {


        // 받아온 id 값을 가지고 정보 찾아오기
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);

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

        MemberEntity memberEntity = memberRepository.findByMemberId(memberId);
        memberEntity.setPhoneNumber(phoneNumber);

        MemberEntity result =  memberRepository.save(memberEntity);
        return result;
    }

    public MemberEntity changePasswordByMypage(MemberEntity memberEntity, String newPassword) {

        // 패스워드 인코딩
        String encodedPassword = passwordEncoder.encode(newPassword);

        // 기존 회원 엔티티에 새로운 비밀번호 설정
        memberEntity.setPassword(encodedPassword);

        // DB에 저장
        MemberEntity result = memberRepository.save(memberEntity);
        return result;
    }

    public MemberEntity changeAddressByMypage(MemberEntity memberEntity, String newAddress) {

        // 기존 회원 엔티티에 새로운 비밀번호 설정
        memberEntity.setAddress(newAddress);

        // DB에 저장
        MemberEntity result = memberRepository.save(memberEntity);
        return result;

    }

    @Transactional
    public void updateMemberImage(MemberDTO memberDTO) {

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

        // 현재 시간을 가져와서 signupDate에 설정
        LocalDateTime currentTime = LocalDateTime.now();
        memberEntity.setSignupDate(currentTime); // 현재 시간을 signupDate에 설정
        memberEntity.setMemberRole("LIMIT");

        MemberDTO result = modelMapper.map(memberEntity , MemberDTO.class);

        return result;

    }

    // 최초 사용자 → 제공자 전환 신청 데이터 저장
    @Transactional
    public AppOwnerInfoDTO registerOwner(@Valid AppOwnerInfoDTO appOwnerInfoDTO) {
        // 1. tbl_member에서 회원 정보 조회
        MemberEntity memberEntity = memberRepository.findByMemberId(appOwnerInfoDTO.getMemberId());

        // 2. owner 테이블에 데이터가 이미 존재하는지 확인 후 삭제
        if (ownerRepository.existsByMemberId(memberEntity.getMemberId())) {
            OwnerInfoEntity existingEntity = ownerRepository.findByMemberId(memberEntity.getMemberId())
                    .orElseThrow(() -> new IllegalStateException("데이터가 존재해야 하지만 찾을 수 없습니다."));
            ownerRepository.delete(existingEntity);
        }

        appOwnerInfoDTO.setIsRejected(0);

        // 3. 새로운 엔티티 생성 및 데이터 설정
        OwnerInfoEntity ownerInfoEntity = new OwnerInfoEntity();
        ownerInfoEntity.setMemberId(memberEntity.getMemberId());
        ownerInfoEntity.setStoreNo(appOwnerInfoDTO.getStoreNo());
        ownerInfoEntity.setStoreName(appOwnerInfoDTO.getStoreName());
        ownerInfoEntity.setStoreAddress(appOwnerInfoDTO.getStoreAddress());
        ownerInfoEntity.setAccount(appOwnerInfoDTO.getAccount());
        ownerInfoEntity.setBank(appOwnerInfoDTO.getBank());
        ownerInfoEntity.setAttechmentLink(appOwnerInfoDTO.getAttechmentLink());
        ownerInfoEntity.setIsRejected(appOwnerInfoDTO.getIsRejected());
        ownerInfoEntity.setStoreImage(appOwnerInfoDTO.getStoreImage());
        ownerInfoEntity.setStorePhone(appOwnerInfoDTO.getStorePhone());

        // 4. 데이터베이스 저장
        OwnerInfoEntity savedEntity = ownerRepository.save(ownerInfoEntity);

        // 5. 저장된 데이터를 DTO로 변환하여 반환
        return new AppOwnerInfoDTO(
                savedEntity.getMemberId(),
                savedEntity.getStoreNo(),
                savedEntity.getStoreName(),
                savedEntity.getStoreAddress(),
                savedEntity.getAccount(),
                savedEntity.getBank(),
                savedEntity.getAttechmentLink(),
                savedEntity.getIsRejected(),
                savedEntity.getStoreImage(),
                savedEntity.getStorePhone()
        );
    }


    // 제공자 신청할 때 최초 신청인지 재신청인지 여부 확인
    public boolean existsByMemberId(String memberId) {
        // Optional을 사용하여 데이터 존재 여부 확인
        Optional<OwnerInfoEntity> ownerInfoEntity = ownerRepository.findByMemberId(memberId);
        return ownerInfoEntity.isPresent(); // 존재하면 true 반환
    }

    // 사용자의 제공자 신청 여부 확인
    public String getOwnerStatus(String memberId) {
        Optional<OwnerInfoEntity> ownerInfoEntity = ownerRepository.findByMemberId(memberId);

        if (ownerInfoEntity.isPresent()) {
            int isRejected = ownerInfoEntity.get().getIsRejected();

            if (isRejected == -1) {
                return "REJECTED";
            } else if (isRejected == 0) {
                return "PENDING";
            } else if (isRejected == 1) {
                return "APPROVED";
            }
        }
        return "NONE"; // 신청 이력이 없음
    }


    // 제공자 재신청 (기존 데이터에서 업데이트로)
    @Transactional
    public AppOwnerInfoDTO upsertOwner(AppOwnerInfoDTO appOwnerInfoDTO) {
        // 1. tbl_member에서 회원 정보 조회
        MemberEntity memberEntity = memberRepository.findByMemberId(appOwnerInfoDTO.getMemberId());

        // 2. owner 테이블에서 기존 데이터 조회
        OwnerInfoEntity existingEntity = ownerRepository.findByMemberId(memberEntity.getMemberId())
                .orElse(null);

        if (existingEntity != null) {
            // 기존 데이터 삭제 (store_no 포함 모든 데이터 삭제)
            ownerRepository.delete(existingEntity);  // Hibernate의 쓰기 지연(flush delay) 문제 방지
            ownerRepository.flush(); // 이거 안 넣으면 insert가 먼저 실행돼서 중복 에러남.
        }

        // 3. 새로운 엔티티 생성 및 저장
        OwnerInfoEntity newEntity = new OwnerInfoEntity();
        newEntity.setMemberId(memberEntity.getMemberId());
        newEntity.setStoreNo(appOwnerInfoDTO.getStoreNo());
        newEntity.setStoreName(appOwnerInfoDTO.getStoreName());
        newEntity.setStoreAddress(appOwnerInfoDTO.getStoreAddress());
        newEntity.setAccount(appOwnerInfoDTO.getAccount());
        newEntity.setBank(appOwnerInfoDTO.getBank());
        newEntity.setAttechmentLink(appOwnerInfoDTO.getAttechmentLink());
        newEntity.setIsRejected(appOwnerInfoDTO.getIsRejected());
        newEntity.setStoreImage(appOwnerInfoDTO.getStoreImage());
        newEntity.setStorePhone(appOwnerInfoDTO.getStorePhone());

        // 4. 저장 (새로운 데이터 삽입)
        OwnerInfoEntity savedEntity = ownerRepository.save(newEntity);

        // 5. 저장된 데이터를 DTO로 변환하여 반환
        return new AppOwnerInfoDTO(
                savedEntity.getMemberId(),
                savedEntity.getStoreNo(),
                savedEntity.getStoreName(),
                savedEntity.getStoreAddress(),
                savedEntity.getAccount(),
                savedEntity.getBank(),
                savedEntity.getAttechmentLink(),
                savedEntity.getIsRejected(),
                savedEntity.getStoreImage(),
                savedEntity.getStorePhone()

        );
    }

    // 제공자 전환 반려 메시지 조회
    public MemberDTO getRejectedMessage(String memberId) {

        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);

        return modelMapper.map(memberEntity , MemberDTO.class);
    }

    public boolean isStoreNoDuplicateOrOwnedByUser(String storeNo, String memberId) {
        // 현재 사용자의 이전 신청 정보 확인
        Optional<OwnerInfoEntity> existingOwnerInfo = ownerRepository.findByMemberId(memberId);

        // 사용자 본인의 이전 store_no인 경우 사용 허용
        if (existingOwnerInfo.isPresent() && existingOwnerInfo.get().getStoreNo().equals(storeNo)) {
            return false;
        }

        // 다른 사용자의 store_no와 중복 확인
        return ownerRepository.existsByStoreNoAndMemberIdNot(storeNo, memberId);
    }

    @Transactional
    public boolean modifyConsulting(String memberId) {

        try{
            MemberEntity findMember = memberRepository.findByMemberId(memberId);
            findMember.setIsConsulting(!findMember.getIsConsulting());
            System.out.println("findMember = " + findMember);

            memberRepository.save(findMember);

            return true;
        } catch (Exception e) {
            System.out.println("e = " + e);

            return false;
        }
    }
}
