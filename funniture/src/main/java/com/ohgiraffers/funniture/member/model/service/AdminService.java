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

//    public List<AppOwnerListModalDTO> getConvertAppListByAdminModal() {
//            return adminRepository.findConvertAppListByAdminModal();
//    }
}
