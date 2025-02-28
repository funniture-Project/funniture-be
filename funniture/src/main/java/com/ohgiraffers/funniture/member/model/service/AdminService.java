package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;
import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import com.ohgiraffers.funniture.member.model.dao.AdminRepository;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final ModelMapper modelMapper;

    // 관리자 페이지에서 모든 사용자 정보 불러오는 로직
    public List<MemberAndPointDTO> getUserListByAdmin() {
        List<Object[]> memberEntityList = adminRepository.AllUserListByAdmin();
        System.out.println("레파지토리에서 잘 조회해 왔는지 memberEntityList = " + memberEntityList);

        return memberEntityList.stream()
                .map(MemberAndPointDTO::new) // ✅ 새로운 생성자 활용
                .collect(Collectors.toList());
    }

    // 관리자 페이지에서 모든 제공자 정보 불러오는 로직
    public List<OwnerInfoAndMemberDTO> getOwnerListByAdmin() {
        List<Object[]> ownerEntityList = adminRepository.findAllOwnerInfo();
        System.out.println("레파지토리에서 잘 조회해 왔는지 ownerEntityList = " + ownerEntityList);

        return ownerEntityList.stream()
                .map(OwnerInfoAndMemberDTO::new) // 생성자 사용하여 변환
                .collect(Collectors.toList());
    }

    // 관리자 페이지에서 제공자 전환 신청 정보 불러오는 로직 (is_result가 0인 항목)
    public List<AppOwnerListDTO> getConvertAppListByAdmin() {
        List<Object[]> memberEntityList = adminRepository.AllConvertListByAdmin();
        System.out.println("레파지토리에서 잘 조회해 왔는지 memberEntityList = " + memberEntityList);

        return memberEntityList.stream()
                .map(AppOwnerListDTO::new) // ✅ 새로운 생성자 활용
                .collect(Collectors.toList());
    }

    // 관리자 페이지에서 모든 탈퇴자 정보 불러오는 로직
    public List<MemberAndPointDTO> getLeaverListByAdmin() {
        List<Object[]> memberEntityList = adminRepository.AllLeaverListByAdmin();
        System.out.println("레파지토리에서 잘 조회해 왔는지 memberEntityList = " + memberEntityList);

        return memberEntityList.stream()
                .map(MemberAndPointDTO::new) // ✅ 새로운 생성자 활용
                .collect(Collectors.toList());
    }

    // 관리자 페이지에서 탈퇴자를 사용자 권한으로 변경하는 로직
    @Transactional
    public boolean leaverToUserApproveService(List<String> userIds) {
        boolean isSuccess = true;

        for (String userId : userIds) {
            System.out.println("✅ 권한 변경 처리 중: 사용자 ID = " + userId);

            // 1. 기존 회원 정보 조회
            MemberAndPointEntity member = adminRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다: " + userId));

            // 2. member_role이 'LIMIT'인 경우만 'USER'로 변경
            if ("LIMIT".equals(member.getMemberRole())) {
                MemberAndPointEntity updatedMember = member.toBuilder()
                        .memberRole("USER") // member_role 값 변경
                        .build();

                // 3. 변경된 객체 저장 (업데이트)
                adminRepository.save(updatedMember);

                System.out.println("✅ 권한이 'USER'로 변경되었습니다: 사용자 ID = " + userId);
            } else {
                System.out.println("❌ 권한 변경 불필요: 사용자 ID = " + userId);
                isSuccess = false; // 권한 변경이 불필요한 경우 실패로 간주
            }
        }
        return isSuccess;
    }

    // 관리자 페이지에서 사용자를 탈퇴자 권한으로 변경하는 로직
    @Transactional
    public boolean userToLeaverApproveService(List<String> userIds) {
        boolean isSuccess = true;

        for (String userId : userIds) {
            System.out.println("✅ 권한 변경 처리 중: 사용자 ID = " + userId);

            // 1. 기존 회원 정보 조회
            MemberAndPointEntity member = adminRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다: " + userId));

            // 2. member_role이 'USER'인 경우만 'LIMIT'로 변경
            if ("USER".equals(member.getMemberRole())) {
                MemberAndPointEntity updatedMember = member.toBuilder()
                        .memberRole("LIMIT") // member_role 값 변경
                        .build();

                // 3. 변경된 객체 저장 (업데이트)
                adminRepository.save(updatedMember);

                System.out.println("✅ 권한이 'LIMIT'로 변경되었습니다: 사용자 ID = " + userId);
            } else {
                System.out.println("❌ 권한 변경 불필요: 사용자 ID = " + userId);
                isSuccess = false; // 권한 변경이 불필요한 경우 실패로 간주
            }
        }
        return isSuccess;
    }

//    public List<AppOwnerListModalDTO> getConvertAppListByAdminModal() {
//            return adminRepository.findConvertAppListByAdminModal();
//    }
}
