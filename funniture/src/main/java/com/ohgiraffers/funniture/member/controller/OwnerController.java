package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.cloudinary.CloudinaryService;
import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.dto.OwnerInfoDTO;
import com.ohgiraffers.funniture.member.model.service.OwnerInfoService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "OWNER API")
@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final AuthController authController;
    private final OwnerInfoService ownerInfoService;

    @Operation(summary = "제공자 전환 신청",
            description = "제공자 전환 시 사업자 정보 저장"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제공자 전환 신청 성공"),
            @ApiResponse(responseCode = "404", description = "제공자 전환 신청 실패")
    })
    @GetMapping("/register")
    public ResponseEntity<ResponseMessage> memberList (@RequestBody OwnerInfoDTO ownerInfoDTO) {

        if (ownerInfoDTO == null) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "회원 정보가 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "로그인 한 회원 목록 조회 성공", null));
    }

    @Operation(summary = "제공자 정보 조회",
            description = "제공자 전호를 이용한 정보 조회",
            parameters = {
                    @Parameter(name = "ownerNo", description = "제공자 ID를 이용하여 정보 조회"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "제공자 정보 조회 성공"),
            @ApiResponse(responseCode = "404", description = "제공자 정보 조회 실패")
    })
    @GetMapping("/{ownerNo}")
    public ResponseEntity<ResponseMessage> getOwnerInfo(@PathVariable String ownerNo){
        OwnerInfoDTO result =  ownerInfoService.getOwnerInfo(ownerNo);

        HttpHeaders headers=  new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> resultMap = new HashMap<>();

        if (result != null) {
            resultMap.put("result", result);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(200,"조회 성공",resultMap));
        } else{
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(404,"조회 실패",null));
        }
    }
}
