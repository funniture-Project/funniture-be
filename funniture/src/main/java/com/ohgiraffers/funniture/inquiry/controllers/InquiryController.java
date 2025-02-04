package com.ohgiraffers.funniture.inquiry.controllers;

import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
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

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Product API")
@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    private HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> findAllInquiry (){

        List<InquiryDTO> result = inquiryService.findAllInquiry();

        Map<String , Object> map = new HashMap<>();
        map.put("result" , result);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200 , "조회 성공",map));
    }

    @GetMapping("/list/{inquiryNo}")
    public ResponseEntity<ResponseMessage> findByInquiryNo(@PathVariable String inquiryNo){

        InquiryDTO inquiry = inquiryService.findByInqiryNo(inquiryNo);

        Map <String , Object> map = new HashMap<>();
        map.put("map", inquiry);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "조회 성공", map));
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> inquiryRegist(@RequestBody InquiryDTO inquiryDTO){

        System.out.println("json에서 들어온 inquiryDTO = " + inquiryDTO);

        String frontNo = "IN";
//        int backNo =

        inquiryService.inquiryRegist(inquiryDTO);

        Map<String , Object> map = new HashMap<>();
        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(201, "등록 성공", map));
    }

}
