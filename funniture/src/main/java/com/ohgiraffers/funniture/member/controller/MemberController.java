package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.MemberService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "로그인 회원 정보 조회",
            description = "로그인 시, 로그인 한 회원에 대한 정보 조회",
            parameters = {
                    @Parameter(name = "memberId", description = "회원 ID를 이용하여 정보 조회"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "회원 정보 조회 실패")
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMessage> memberList (@PathVariable String memberId) {
        System.out.println("화면에서 넘어온 memberId"+ memberId);
        System.out.println("✅ memberList 조회를 위한 memberList 컨트롤러 동작..");

        MemberDTO memberDTO = memberService.getMemberList(memberId);
        System.out.println("✅ 서비스에서 넘어온 로그인 회원 목록 = " + memberDTO);

        memberDTO.setPassword(null);
        Map<String , Object> result = new HashMap<>();
        result.put("result" , memberDTO);

        if (memberDTO == null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "회원 정보가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "로그인 한 회원 목록 조회 성공", result));
    }
}
