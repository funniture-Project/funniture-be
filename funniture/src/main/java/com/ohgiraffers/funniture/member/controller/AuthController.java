package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.AuthService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup (@RequestBody MemberDTO memberDTO) {

        System.out.println("회원가입 프론트에서 들어온 memberDTO = " + memberDTO);

        String newMemberNo = authService.getMaxMember();
        String newNo = returnMemberNo(newMemberNo);
        System.out.println("신규 생성된 newNo = " + newNo);
        memberDTO.setMemberId(newNo);

        MemberDTO member = authService.authSignupService(memberDTO);

        Map<String , Object> result = new HashMap<>();
        result.put("result", member);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "회원가입 성공",result));
    }

    public String returnMemberNo(String maxMember){
        if (maxMember == null || maxMember.isEmpty()){
            return "MEM001";
        } else {
            int newMemberNo = Integer.parseInt(maxMember.substring(3)) + 1;
            System.out.println("newMemberNo = " + newMemberNo);
            return String.format("MEM%03d",newMemberNo);
        }
    }

}
