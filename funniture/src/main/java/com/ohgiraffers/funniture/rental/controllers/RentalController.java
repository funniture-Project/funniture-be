package com.ohgiraffers.funniture.rental.controllers;

import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
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

    // 사용자 예약 등록
    @PostMapping("user/insert")
    public ResponseEntity<ResponseMessage> insertRental(@RequestBody RentalDTO rentalDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        rentalService.insertRental(rentalDTO);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(201, "등록완료", null));
    }

    // 사용자 조회(주문/배송)
    @GetMapping("user/orderList")
    public ResponseEntity<ResponseMessage> findRentalOrderListByUser(){


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 나중에 로그인한 사람 code 꺼내서 가지고 가야함, 일단 조인시켜 조회만 해놓기!!
        List<UserOrderViewDTO> orderList = rentalService.findRentalOrderListByUser();

        Map<String, Object> res = new HashMap<>();
        res.put("orderList", orderList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "정상조회", res));
    }

    // 관리자 조회(예약정보 - 예약전체리스트)
    @GetMapping("admin/allList")
    public ResponseEntity<ResponseMessage> findRentalAllListByAdmin() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<AdminRentalViewDTO> adminRentalList = rentalService.findRentalAllListByAdmin();

        Map<String, Object> res = new HashMap<>();
        res.put("adminRentalList", adminRentalList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "정상조회", res));
    }

    // 관리자 조회(예약상세정보(사용자,제공자 정보))


}
