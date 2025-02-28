package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.model.dto.*;
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

    @Operation(summary = "전체 탈퇴자 정보 조회",
            description = "관리자 페이지에서 모든 탈퇴자 정보 조회"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "모든 탈퇴자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "모든 탈퇴자 정보 조회 실패")
    })
    @GetMapping("/leaverList")
    public ResponseEntity<ResponseMessage> leaverListByAdmin () {
        System.out.println("✅ 관리자 페이지에서 탈퇴자 데이터 불러오는 컨트롤러 동작");

        List<MemberAndPointDTO> memberAndPointDTO = adminService.getLeaverListByAdmin();
        System.out.println("✅ 관리자 페이지에서 탈퇴자 데이터 서비스 갔다가 컨트롤러 = " + memberAndPointDTO);

        Map<String , Object> result = new HashMap<>();
        result.put("result" , memberAndPointDTO);

        if (memberAndPointDTO.isEmpty()) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "모든 탈퇴자 정보가 존재하지 않음.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "모든 탈퇴자 정보 조회 성공", result));
    }

    @Operation(summary = "탈퇴자 권한 정보 변경",
            description = "관리자 페이지에서 탈퇴자를 유저로 권한 변경"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "탈퇴자 회원, 유저로 권한 변경 성공"),
            @ApiResponse(responseCode = "404", description = "탈퇴자 회원, 유저로 권한 변경 실패")
    })
    @PostMapping("/reactivate")
    public ResponseEntity<ResponseMessage> leaverToUserApproveByAdmin (@RequestBody List<String> userIds) {
        System.out.println("✅ 관리자 페이지에서 탈퇴자 → 유저 권한 변경 컨트롤러 userIds 잘 받아왔나 : " + userIds);

        Boolean isSuccess = adminService.leaverToUserApproveService(userIds);

        if (isSuccess) {
            // 성공 응답
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201, "탈퇴자 회원, 유저로 권한이 성공적으로 변경되었습니다.", null));
        } else {
            // 실패 응답
            return ResponseEntity.ok() // 상태 코드 400 (Bad Request)
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400, "탈퇴자 회원 중 일부 또는 전체가 이미 유저 권한입니다.", null));
        }
    }

    @Operation(summary = "사용자 권한 정보 변경",
            description = "관리자 페이지에서 사용자를 탈퇴자로 권한 변경"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "사용자자 회원, 탈퇴자로 권한 변경 성공"),
            @ApiResponse(responseCode = "404", description = "사용자자 회원, 탈퇴자로 권한 변경 실패")
    })
    @PostMapping("/deactivate")
    public ResponseEntity<ResponseMessage> userToLeaverApproveByAdmin (@RequestBody List<String> userIds) {
        System.out.println("✅ 관리자 페이지에서 유저 → 탈퇴자 권한 변경 컨트롤러 userIds 잘 받아왔나 : " + userIds);

        Boolean isSuccess = adminService.userToLeaverApproveService(userIds);

        if (isSuccess) {
            // 성공 응답
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(201, "사용자 회원, 탈퇴자로 권한이 성공적으로 변경되었습니다.", null));
        } else {
            // 실패 응답
            return ResponseEntity.ok() // 상태 코드 400 (Bad Request)
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(400, "사용자 회원 중 일부 또는 전체가 이미 탈퇴자 권한입니다.", null));
        }
    }


//    @Operation(summary = "모달에 표시될 제공자 전환 데이터",
//            description = "관리자 페이지에서 모달에 표시될 제공자로 전환 요청 정보 조회"
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "제공자로 전환 정보 조회 성공(모달 데이터)"),
//            @ApiResponse(responseCode = "404", description = "제공자로 전환 정보 조회 실패(모달 데이터)")
//    })
//    @GetMapping("/convertApp/modal")
//    public ResponseEntity<ResponseMessage> convertListByAdminModal () {
//        System.out.println("✅ 관리자 페이지에서 모달 표시될 제공자 전환 데이터 불러오는 컨트롤러 동작");
//
//        List<AppOwnerListModalDTO> appOwnerListModalDTO = adminService.getConvertAppListByAdminModal();
//        System.out.println("✅ 관리자 페이지에서 모달 표시될 유저 정보 서비스 갔다가 컨트롤러 = " + appOwnerListModalDTO);
//
//        Map<String , Object> result = new HashMap<>();
//        result.put("result" , appOwnerListModalDTO);
//
//        if (appOwnerListModalDTO.isEmpty()) {
//            return ResponseEntity.ok()
//                    .headers(authController.headersMethod())
//                    .body(new ResponseMessage(404, "모든 제공자 전환신청 정보가 존재하지 않음(모달 데이터).", null));
//        }
//
//        return ResponseEntity.ok()
//                .headers(authController.headersMethod())
//                .body(new ResponseMessage(200, "모든 제공자 전환신청 정보 조회 성공(모달 데이터)", result));
//    }
}
