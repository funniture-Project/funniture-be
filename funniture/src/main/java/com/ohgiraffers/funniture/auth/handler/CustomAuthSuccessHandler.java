package com.ohgiraffers.funniture.auth.handler;

import com.ohgiraffers.funniture.common.AuthConstants;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.dto.TokenDTO;
import com.ohgiraffers.funniture.util.ConvertUtil;
import com.ohgiraffers.funniture.util.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        MemberDTO member  = ((MemberDTO) authentication.getPrincipal());

        HashMap<String, Object> responseMap = new HashMap<>();
        JSONObject jsonValue = null;
        JSONObject jsonObject;

        if(member.getMemberRole().equals("LIMIT")){
            responseMap.put("userInfo", jsonValue);
            responseMap.put("status", 500);
            responseMap.put("message","휴먼상태인 계정입니다.");
        }

        else{

            String token = TokenUtils.generateJwtToken(member);
            // tokenDTO response
            TokenDTO tokenDTO = TokenDTO.builder()
                                .memberName(member.getUsername())
                                .accessToken(token)
                                .grantType(AuthConstants.TOKEN_TYPE)
                                .memberRole(member.getMemberRole())
                                .memberId(member.getMemberId())
                                .build();

            jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(tokenDTO);
            responseMap.put("userInfo", jsonValue);
            responseMap.put("status", 200);
            responseMap.put("message", "로그인 성공");
        }

        jsonObject = new JSONObject(responseMap);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}
