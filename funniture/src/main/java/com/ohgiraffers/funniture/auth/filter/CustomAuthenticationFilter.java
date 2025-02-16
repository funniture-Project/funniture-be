package com.ohgiraffers.funniture.auth.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    // 지정된 url 요청 시, 요청 가로채서 검증 로직 수행 메소드
    @Override
    @Bean
    public Authentication attemptAuthentication (HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("✅ CustomAuthenticationFilter - attemptAuthentication 시작");
        // 토큰 생성
        UsernamePasswordAuthenticationToken authRequest;

        try {
            authRequest = getAuthRequest(request); // json 데이터에서 ID, PW 추출
            setDetails(request, authRequest); // 요청 정보 설정
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.getAuthenticationManager().authenticate(authRequest); // 인증 수행
    }

    // 사용자의 로그인 리소스 요청 시 요청 정보를 임시 토큰에 저장하는 메소드
    private UsernamePasswordAuthenticationToken getAuthRequest (HttpServletRequest request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        //  JSR310 모듈 등록 (LocalDateTime 지원)
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);

        MemberDTO member = objectMapper.readValue(request.getInputStream(), MemberDTO.class);

        return new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPassword());
    }
}
