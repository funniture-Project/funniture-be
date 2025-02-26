package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.model.dto.MemberAndPointDTO;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.AdminService;
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

@Tag(name = "ADMIN API")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthController authController;
    private final CloudinaryService cloudinaryService;

    @Operation(summary = "전체 유저 정보 조회",
            description = "관리자 페이지에서 모든 유저 정보 조회"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 유저 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "모든 유저 정보 조회 실패")
    })
    @GetMapping("/userList")
    public ResponseEntity<ResponseMessage> userListByAdmin () {
        System.out.println("✅ 관리자 페이지에서 유저 정보 불러오는 컨트롤러 동작");

        List<MemberAndPointDTO> MemberAndPointDTO = adminService.getUserListByAdmin();
        System.out.println("✅ 관리자 페이지에서 유저 정보 서비스 갔다가 컨트롤러 = " + MemberAndPointDTO);

//        memberDTO.setPassword(null);
        Map<String , Object> result = new HashMap<>();
        result.put("result" , MemberAndPointDTO);

        if (MemberAndPointDTO == null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "모든 유저 정보가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "전체 회원 조회 성공", result));
    }

}
