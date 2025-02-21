package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.member.entity.MemberEntity;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.MemberService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "LOGIN API")
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

    @Operation(summary = "로그인 페이지 비밀번호 변경",
            description = "로그인 페이지에서 비밀번호 변경")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "404", description = "비밀번호 변경 실패")
    })
    @PostMapping ("/findPass")
    public ResponseEntity<ResponseMessage> changePasswordByLogin (@RequestBody MemberDTO memberDTO) {

        System.out.println("프론트에서 패스워드 변경 요청 잘 들어 왔나? email = " + memberDTO.getEmail());
        System.out.println("프론트에서 패스워드 변경 요청 잘 들어 왔나? password = " + memberDTO.getPassword());

        MemberEntity memberEntity = memberService.findByEmail(memberDTO.getEmail());
        System.out.println("이메일에 해당하는 값이 있나? memberEntity = " + memberEntity);

        // 서비스로 비밀번호 바꿀 정보 넘겨주기
        MemberEntity result = memberService.changePassword(memberEntity, memberDTO.getPassword());

        if(result.getPassword() != null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201 , "비밀번호 변경 완료", null));
        } else {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404 , "비밀번호 변경 실패", null));
        }
    }
}
