package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.model.dto.AppOwnerListDTO;
import com.ohgiraffers.funniture.member.model.dto.MemberAndPointDTO;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.dto.OwnerInfoAndMemberDTO;
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

        List<MemberAndPointDTO> memberAndPointDTO = adminService.getUserListByAdmin();
        System.out.println("✅ 관리자 페이지에서 유저 정보 서비스 갔다가 컨트롤러 = " + memberAndPointDTO);

        Map<String , Object> result = new HashMap<>();
        result.put("result" , memberAndPointDTO);

        if (memberAndPointDTO.isEmpty()) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "모든 사용자 정보가 존재하지 않음", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "모든 사용자 정보 조회 성공", result));
    }

    @Operation(summary = "전체 제공자 정보 조회",
            description = "관리자 페이지에서 모든 제공자 정보 조회"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 제공자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "모든 제공자 정보 조회 실패")
    })
    @GetMapping("/ownerList")
    public ResponseEntity<ResponseMessage> ownerListByAdmin() {
        System.out.println("✅ 관리자 페이지에서 제공자 정보 불러오는 컨트롤러 동작");

        List<OwnerInfoAndMemberDTO> ownerInfoAndMemberDTO = adminService.getOwnerListByAdmin();
        System.out.println("제공자 정보 서비스에서 잘 넘어 왔는지 ownerInfoAndMemberDTO = " + ownerInfoAndMemberDTO);

        Map<String, Object> result = new HashMap<>();
        result.put("result", ownerInfoAndMemberDTO);

        if (ownerInfoAndMemberDTO.isEmpty()) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "모든 제공자 정보가 존재하지 않음.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "모든 제공자 정보 조회 성공", result));
    }

    @Operation(summary = "전체 유저 정보 조회",
            description = "관리자 페이지에서 모든 유저 정보 조회"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 유저 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "모든 유저 정보 조회 실패")
    })
    @GetMapping("/convertApp")
    public ResponseEntity<ResponseMessage> convertListByAdmin () {
        System.out.println("✅ 관리자 페이지에서 제공자 전환 데이터 불러오는 컨트롤러 동작");

        List<AppOwnerListDTO> appOwnerListDTO = adminService.getConvertAppListByAdmin();
        System.out.println("✅ 관리자 페이지에서 유저 정보 서비스 갔다가 컨트롤러 = " + appOwnerListDTO);

        Map<String , Object> result = new HashMap<>();
        result.put("result" , appOwnerListDTO);

        if (appOwnerListDTO.isEmpty()) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "모든 제공자 전환신청 정보가 존재하지 않음.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "모든 제공자 전환신청 정보 조회 성공", result));
    }
}
