package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.member.model.dto.MemberDTO;
import com.ohgiraffers.funniture.member.model.service.AuthService;
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

@Tag(name="SIGNUP API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    public HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

    @Operation(summary = "회원 가입",
            description = "회원 가입 시, 회원 등록"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 등록 성공"),
    })
    @PostMapping("/signup")
    public ResponseEntity<ResponseMessage> signup (@RequestBody MemberDTO memberDTO) {

//        System.out.println("회원가입 프론트에서 들어온 memberDTO = " + memberDTO);

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

    // 회원가입 시, 중복된 이메일(회원 있는지 확인)
    @Operation(summary = "중복 검사 로직",
            description = "회원 가입 시, 중복된 이메일인지 검증"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원 가입 가능한 이메일"),
            @ApiResponse(responseCode = "400",description = "중복된 이메일")
    })
    @GetMapping("/validation/{email}")
    public ResponseEntity<ResponseMessage> withdrawByMemberId(@PathVariable String email){
//        System.out.println("최초 프론트에서 회원가입 이메일 들어왔나 = " + email);

        Boolean result = authService.validationDuplicateEmail(email);

        Map <String , Object> response = new HashMap<>();

        response.put("response" , result);

        // false 라면 동일 이메일 없으므로 회원가입 가능
        if (!result){
            return ResponseEntity.ok()
                    .headers(headersMethod())
                    .body(new ResponseMessage(200, "회원가입 가능한 이메일 입니다.", response));
        } else {
            return ResponseEntity.ok()
                    .headers(headersMethod())
                    .body(new ResponseMessage(400, "중복된 이메일이 존재합니다.", response));
        }
    }

}
