package com.ohgiraffers.funniture.auth.handler;

import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService detailsService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken loginToken = (UsernamePasswordAuthenticationToken) authentication;

        String email = loginToken.getName();
        String password = (String) loginToken.getCredentials();

        System.out.println("✅ Authentication authenticate 동작 : email = " + email);
        System.out.println("✅ Authentication authenticate 동작 : password = " + password);

        MemberDTO member = (MemberDTO) detailsService.loadUserByUsername(email);
        System.out.println("✅ Authentication authenticate에서  = " + member);

        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new BadCredentialsException(password + "는 비밀번호가 아닙니다.");
        }
        System.out.println("✅ 일단조회" + member.getAuthorities());
        return new UsernamePasswordAuthenticationToken(member, password, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {

        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
