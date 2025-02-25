package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO;
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
import java.util.Map;

@Tag(name = "OWNER API")
@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final AuthController authController;
    private final CloudinaryService cloudinaryService;

    @Operation(summary = "제공자 전환 신청",
            description = "제공자 전환 시 사업자 정보 저장"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제공자 전환 신청 성공"),
            @ApiResponse(responseCode = "404", description = "제공자 전환 신청 실패")
    })
    @GetMapping("/register")
    public ResponseEntity<ResponseMessage> memberList (@RequestBody OwnerInfoDTO ownerInfoDTO) {
        System.out.println("화면에서 넘어온 ownerInfoDTO"+ ownerInfoDTO);

//        OwnerInfoDTO ownerInfoDTO = memberService.getMemberList(memberId);
//        System.out.println("✅ 서비스에서 넘어온 로그인 회원 목록 = " + memberDTO);

        if (ownerInfoDTO == null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "회원 정보가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "로그인 한 회원 목록 조회 성공", null));
    }
}
