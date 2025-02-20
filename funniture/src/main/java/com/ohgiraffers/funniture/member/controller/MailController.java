package com.ohgiraffers.funniture.member.controller;

import com.ohgiraffers.funniture.inquiry.controllers.InquiryController;
import com.ohgiraffers.funniture.member.model.service.MailService;
import com.ohgiraffers.funniture.response.ResponseMessage;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class MailController {

    private final MailService mailService;

    @PostMapping("/{email}")
    public ResponseEntity<ResponseMessage> sendEmail(@PathVariable String email) throws MessagingException {

        System.out.println("이메일 잘 들어왔는지 email = " + email);
        boolean isSend = mailService.sendSimpleMessage(email);

        System.out.println("이메일 서비스 잘 다녀 왔는지 isSend = " + isSend);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String , Object> map = new HashMap<>();
        map.put("result",isSend);

        if (isSend){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(200 , "이메일 전송 성공",map));
        } else {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(404, "등록된 문의가 없습니다.", null));
        }

    }

}
