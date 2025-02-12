package com.ohgiraffers.funniture.inquiry.controllers;

import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.MemberDTO;
import com.ohgiraffers.funniture.inquiry.model.service.InquiryService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "OWNER API")
@RestController
@RequestMapping("/api/v1/owner/inquiry")
@RequiredArgsConstructor
public class OwnerController {

    private final InquiryService inquiryService;

    private HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

    // member_id에 따른 제공자 페이지의 전체 문의들
    @GetMapping ("")
    public ResponseEntity<ResponseMessage> findAllOwnerPageInquiry () {
        List<MemberDTO> result = inquiryService.findByInquiryOwnerPage();

        Map <String , Object> map = new HashMap<>();
        map.put("map", result);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "조회 성공", map));
    }

    // 답변하지 않은 문의들
    @GetMapping ("/wait")
    public ResponseEntity<ResponseMessage> findWaitOwnerPageInquiry () {

        return null;
    }

    // 답변하지 않은 문의들
    @GetMapping ("/complete")
    public ResponseEntity<ResponseMessage> findCompleteOwnerPageInquiry () {

        return null;
    }

    // 문의 답변 수정하기
    @PutMapping("/modify/{inquiryNo}")
    public ResponseEntity<ResponseMessage> inquiryModify(@PathVariable String inquiryNo
            ,@RequestBody InquiryDTO inquiryDTO){

        System.out.println("컨트롤러 : 화면에서 inquiryNo 받아오나 = " + inquiryNo);

        inquiryService.modifyByInquiryNo(inquiryNo,inquiryDTO);

        Map<String , Object> map = new HashMap<>();

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(201, "수정 성공", map));
    }
}
