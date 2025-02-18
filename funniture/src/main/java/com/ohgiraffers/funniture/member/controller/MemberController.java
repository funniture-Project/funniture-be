package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.member.model.service.MemberService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private MemberService memberService;

    private AuthController authController;

//    @GetMapping
//    public ResponseEntity<ResponseMessage> memberList () {
//
//        return ResponseEntity.ok()
//                .headers(authController.headersMethod())
//                .body(new ResponseMessage(200, "회원 전체 목록 조회 성공", result));
//    }
}
