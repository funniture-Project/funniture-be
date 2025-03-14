package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.inquiry.controllers.InquiryController;
import com.ohgiraffers.funniture.member.model.service.MailService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
@Tag(name = "EMAIL API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "인증번호 발송",
            description = "회원 가입 시 이메일로 인증번호 발송",
            parameters = {
                    @Parameter(name = "email", description = "회원 email에 인증번호 발송"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 전송 성공"),
            @ApiResponse(responseCode = "404", description = "이메일 전송 실패")
    })
    @PostMapping("/{email}")
    public ResponseEntity<ResponseMessage> sendEmail(@PathVariable String email) throws MessagingException {

        System.out.println("이메일 잘 들어왔는지 email = " + email);
        String isSend = mailService.sendSimpleMessage(email);

        System.out.println("이메일 서비스 잘 다녀 왔는지 isSend = " + isSend);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String , Object> map = new HashMap<>();
        map.put("result",isSend);

        if (isSend != null){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(200 , "이메일 전송 성공",map));
        } else {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(404, "이메일 전송 실패", null));
        }

    }

}
