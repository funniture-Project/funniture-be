package com.ohgiraffers.funniture.member.model.service;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dao.MemberRepository;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private MemberRepository memberRepository;
    private ModelMapper modelMapper;

    public CustomUserDetailsService (MemberRepository memberRepository,
                                     ModelMapper modelMapper) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("✅ loadUserByUsername 동작");

        MemberEntity member = memberRepository.findByEmail(email);

        System.out.println("✅ loadUserByUsername member : " + member);
        if (member == null) {
            throw new UsernameNotFoundException("해당 ID의 사용자를 찾을 수 없습니다: " + email);
        }

        // MemberEntity -> MemberDTO 변환
        MemberDTO memberDTO = modelMapper.map(member, MemberDTO.class);
        System.out.println("✅ loadUserByUsername에서 회원 정보 찾아온 거 dto로 변환 = " + memberDTO);

        // String 컬럼을 이용해 권한을 GrantedAuthority 리스트로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getMemberRole().toUpperCase()));
        System.out.println("✅ 권한 : ");
        System.out.println(authorities);

        memberDTO.setAuthorities(authorities);

        return memberDTO;
    }

    public List<Map<String, String>> findAllOwner() {
        List<Map<String, String>> result = new ArrayList<>();

        List<Object[]> ownerList = memberRepository.findAllOwner();

        for (Object[] owner : ownerList){
            Map<String, String> ownerInfo = new HashMap<>();

            ownerInfo.put("store_name", (String) owner[0]);
            ownerInfo.put("owner_no", (String) owner[1]);

            result.add(ownerInfo);
        }

        return result;
    }
}
