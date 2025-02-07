package com.ohgiraffers.funniture.rental.controllers;

import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import com.ohgiraffers.funniture.rental.model.service.RentalService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Tag : 관련 있는 API 들의 그룹을 짓기 위한 어노테이션
@Tag(name = "Rental API")
@RestController
@RequestMapping("/api/v1/rental")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    // 예약등록
    @PostMapping("/insert")
    public ResponseEntity<ResponseMessage> insertRental(@RequestBody RentalDTO rentalDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        rentalService.insertRental(rentalDTO);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(201, "등록완료", null));
    }

    // 관리자(예약정보 - 예약전체리스트)
    @GetMapping("/allList")
    public ResponseEntity<ResponseMessage> findRentalAllListByAdmin() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "정상조회", null));
    }
}
