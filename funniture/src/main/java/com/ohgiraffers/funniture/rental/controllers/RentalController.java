package com.ohgiraffers.funniture.rental.controllers;

import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalSearchCriteria;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import com.ohgiraffers.funniture.rental.model.service.RentalService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> insertRental(@RequestBody RentalDTO rentalDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        rentalService.insertRental(rentalDTO);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(201, "등록완료", null));
    }

    // 사용자의 예약 전체 조회(주문/배송)
    @GetMapping("/user")
    public ResponseEntity<ResponseMessage> findRentalOrderListByUser(@RequestParam(required = false) String period,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 나중에 로그인한 사람 code 꺼내서 가지고 가야함, 일단 조인시켜 조회만 해놓기!!
        List<UserOrderViewDTO> orderList = rentalService.findRentalOrderListByUser(period,searchDate);

        Map<String, Object> res = new HashMap<>();
        res.put("orderList", orderList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "정상조회", res));
    }

    // 예약 상세 조회(사용자, 제공자 동일) /{rentalNo}


    // 관리자 예약 전체조회 -> 사용자, 제공자 정보는 user 폴더에!
    @GetMapping
    public ResponseEntity<ResponseMessage> findRentalAllListByAdmin(@RequestParam(required = false) String rentalState,
                                                                    @RequestParam(required = false) String storeName,
                                                                    @RequestParam(required = false) String categoryName,
                                                                    @RequestParam(required = false) String searchDate,
                                                                    @RequestParam(required = false) String rentalNo
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        System.out.println("searchDate = " + searchDate);

        LocalDateTime searchDateTime =
                LocalDate.parse(searchDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                .atStartOfDay();

        System.out.println("searchDateTime = " + searchDateTime);

        // 검색 조건 DTO로 변환
        AdminRentalSearchCriteria criteria = new AdminRentalSearchCriteria(rentalState, storeName, categoryName, searchDateTime, rentalNo);

        System.out.println("criteria = " + criteria);

        List<AdminRentalViewDTO> adminRentalList = rentalService.findRentalAllListByAdmin(criteria);

        Map<String, Object> res = new HashMap<>();
        res.put("adminRentalList", adminRentalList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "정상조회", res));
    }



}
