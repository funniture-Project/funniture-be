package com.ohgiraffers.funniture.auth.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    // 지정된 url 요청 시, 요청 가로채서 검증 로직 수행 메소드
    @Override
    public Authentication attemptAuthentication (HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;



        return null;
    }

    // 사용자의 로그인 리소스 요청 시 요청 정보를 임시 토큰에 저장하는 메소드
//    private UsernamePasswordAuthenticationToken getAuthRequest (){}
}
