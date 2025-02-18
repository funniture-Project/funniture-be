package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.MemberService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final AuthController authController;

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMessage> memberList (@PathVariable String memberId) {
        System.out.println("화면에서 넘어온 memberId"+ memberId);
        System.out.println("✅ memberList 조회를 위한 memberList 컨트롤러 동작..");

        MemberDTO memberDTO = memberService.getMemberList(memberId);
        System.out.println("✅ 서비스에서 넘어온 로그인 회원 목록 = " + memberDTO);
        Map<String , Object> result = new HashMap<>();
        result.put("result" , memberDTO);

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "로그인 한 회원 목록 조회 성공", result));
    }
}
