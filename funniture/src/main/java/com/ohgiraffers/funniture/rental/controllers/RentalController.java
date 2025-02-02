package com.ohgiraffers.funniture.rental.controllers;

import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import com.ohgiraffers.funniture.rental.model.service.RentalService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Rental API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // 관리자(예약정보 - 예약전체리스트)
    @GetMapping("/v1/rental/allList")
    public ResponseEntity<ResponseMessage> findRentalAllListByAdmin() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<RentalDTO> allList = rentalService.findRentalAllListByAdmin();

        Map<String, Object> res = new HashMap<>();
        res.put("allList", allList);
        System.out.println("res = " + res);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "정상조회", res));
    }



}
