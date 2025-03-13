package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PageDTO;
import com.ohgiraffers.funniture.common.PagingResponseDTO;
import com.ohgiraffers.funniture.member.entity.MemberAndPointEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import com.ohgiraffers.funniture.member.model.dao.AdminRepository;
import com.ohgiraffers.funniture.member.model.dao.OwnerRepository;
import com.ohgiraffers.funniture.member.model.dto.*;
import com.ohgiraffers.funniture.point.entity.PointEntity;
import com.ohgiraffers.funniture.point.model.dao.PointRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final OwnerRepository ownerRepository;
    private final ModelMapper modelMapper;
    private final PointRepository pointRepository;

    // 관리자 페이지에서 모든 사용자 정보 불러오는 로직
    public PagingResponseDTO getUserListByAdmin(Criteria cri) {
        int total = adminRepository.countAllUsers();
        int offset = (cri.getPageNum() - 1) * cri.getAmount();
        List<Object[]> memberEntityList = adminRepository.AllUserListByAdmin(offset, cri.getAmount());

        List<MemberAndPointDTO> memberAndPointDTOList = memberEntityList.stream()
                .map(MemberAndPointDTO::new)
                .collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(memberAndPointDTOList);
        pagingResponseDTO.setPageInfo(pageInfo);

        return pagingResponseDTO;
    }

    // 관리자 페이지에서 모든 탈퇴자 정보 불러오는 로직
    public PagingResponseDTO getLeaverListByAdmin(Criteria cri) {
        int total = adminRepository.countAllLeavers();
        int offset = (cri.getPageNum() - 1) * cri.getAmount();

        List<Object[]> memberEntityList = adminRepository.AllLeaverListByAdmin(offset, cri.getAmount());

        List<MemberAndPointDTO> memberAndPointDTOList = memberEntityList.stream()
                .map(MemberAndPointDTO::new)
                .collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(memberAndPointDTOList);
        pagingResponseDTO.setPageInfo(pageInfo);

        return pagingResponseDTO;
    }

    // 관리자 페이지에서 모든 제공자 정보 불러오는 로직
    public PagingResponseDTO getOwnerListByAdmin(Criteria cri) {
        int total = adminRepository.countAllOwners();
        int offset = (cri.getPageNum() - 1) * cri.getAmount();

        List<Object[]> ownerEntityList = adminRepository.findAllOwnerInfo(offset, cri.getAmount());

        List<OwnerInfoAndMemberDTO> ownerInfoList = ownerEntityList.stream()
                .map(OwnerInfoAndMemberDTO::new)
                .collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(ownerInfoList);
        pagingResponseDTO.setPageInfo(pageInfo);

        return pagingResponseDTO;
    }

    // 관리자 페이지에서 제공자 전환 신청 정보 불러오는 로직 (is_result가 0인 항목)
    public PagingResponseDTO getConvertAppListByAdmin(Criteria cri) {
        int total = adminRepository.countAllConvertApps();
        int offset = (cri.getPageNum() - 1) * cri.getAmount();

        List<Object[]> memberEntityList = adminRepository.AllConvertListByAdmin(offset, cri.getAmount());

        List<AppOwnerListDTO> appOwnerListDTOs = memberEntityList.stream()
                .map(AppOwnerListDTO::new)
                .collect(Collectors.toList());

        PageDTO pageInfo = new PageDTO(cri, total);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        pagingResponseDTO.setData(appOwnerListDTOs);
        pagingResponseDTO.setPageInfo(pageInfo);

        return pagingResponseDTO;
    }


    // 관리자 페이지에서 탈퇴자를 사용자 권한으로 변경하는 로직
    @Transactional
    public boolean leaverToUserApproveService(List<String> userIds) {
        boolean isSuccess = true;

        for (String userId : userIds) {

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

    // 관리자 페이지에서 제공자 전환 신청한 애들 눌렀을 때 모달에 표시될 데이터 부르는 로직
    public AppOwnerListModalDTO getConvertAppDetailByAdmin(String memberId) {

        return adminRepository.findConvertAppDetailByMemberId(memberId);
    }

    // 관리자 페이지에서 제공자 애들 눌렀을 때 모달에 표시될 데이터 부르는 로직
    public AppOwnerListModalDTO getOwnerDetailByAdmin(String memberId) {

        return adminRepository.findOwnerDetailByMemberId(memberId);
    }

    // 제공자 전환 신청 모달에서 승인 눌렀을 때 동작하는 애
    @Transactional
    public Boolean approveUserToOwnerByAdmin(String memberId) {
        try {
            // 1. 기존 회원 정보 조회
            MemberAndPointEntity member = adminRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다: " + memberId));

            // 2. member_role을 'OWNER'로 변경
            MemberAndPointEntity updatedMember = member.toBuilder()
                    .memberRole("OWNER")
                    .build();

            // 3. 변경된 객체 저장 (업데이트)
            adminRepository.save(updatedMember);

            // 4. OwnerInfoEntity의 is_rejected를 1로 변경
            OwnerInfoEntity ownerInfo = ownerRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new RuntimeException("제공자 정보를 찾을 수 없습니다: " + memberId));

            ownerInfo.setIsRejected(1);
            ownerRepository.save(ownerInfo);

            System.out.println("✅ 권한이 'OWNER'로 변경되었고, is_rejected가 1로 설정되었습니다: 사용자 ID = " + memberId);
            return true;
        } catch (Exception e) {
            System.out.println("❌ 제공자 전환 승인 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 관리자가 제공자 전환 신청자 반려 눌렀을 때
    public Boolean rejectUserToOwnerByAdmin(String memberId, String rejectReason) {
        try {
            // 1. 기존 회원 정보 조회
            MemberAndPointEntity member = adminRepository.findById(memberId)
                    .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다: " + memberId));

            // 2. member_role을 'USER'로 변경하고, 반려 사유 저장
            MemberAndPointEntity updatedMember = member.toBuilder()
                    .memberRole("USER")
                    .reasonRejection(rejectReason) // 반려 사유 저장
                    .build();
            adminRepository.save(updatedMember);

            // 3. OwnerInfoEntity 조회 후 is_rejected를 -1로 변경
            OwnerInfoEntity ownerInfo = ownerRepository.findByMemberId(memberId)
                    .orElseThrow(() -> new RuntimeException("제공자 정보를 찾을 수 없습니다: " + memberId));

            ownerInfo.setIsRejected(-1); // 반려 상태로 변경
            ownerRepository.save(ownerInfo); // 삭제 대신 저장하여 기록 유지

            System.out.println("✅ 제공자 전환 요청이 반려되었습니다. 사용자 ID = " + memberId);
            return true;
        } catch (Exception e) {
            System.out.println("❌ 제공자 전환 반려 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

    // 관리자 페이지에서 사용자 포인트 수정하는 구문
    @Transactional
    public Boolean userPointUpdateService(String memberId, int newPoint) {
        try {
            MemberAndPointEntity member = adminRepository.findById(memberId)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found"));

            // MemberAndPointEntity 업데이트
            MemberAndPointEntity updatedMember = member.toBuilder()
                    .currentPoint(newPoint)
                    .build();
            adminRepository.save(updatedMember);

            // PointEntity 생성 및 저장
            PointEntity pointEntity = PointEntity.builder()
                    .memberId(memberId)
                    .currentPoint(newPoint)
                    .addPoint(newPoint - member.getCurrentPoint()) // 포인트 증가분
                    .usedPoint(0) // 사용된 포인트는 0으로 설정
                    .build();
            pointRepository.save(pointEntity);

            return true;
        } catch (Exception e) {
            System.out.println("❌ 포인트 수정 중 오류 발생: " + e.getMessage());
            return false;
        }
    }

}
