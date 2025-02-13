package com.ohgiraffers.funniture.rental.controllers;

import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalSearchCriteria;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import com.ohgiraffers.funniture.rental.model.service.RentalService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "사용자 상품 예약 등록",
            description = "예약 등록 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 등록이 완료되었습니다.")
    })
    // 사용자 예약 등록
    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> insertRental(@RequestBody RentalDTO rentalDTO) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        rentalService.insertRental(rentalDTO);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(201, "예약 등록이 완료되었습니다.", null));
    }

    @Operation(summary = "사용자의 예약 전체 조회",
            description = "사용자 마이페이지 주문/배송에서 사용",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID(필수)"),
                    @Parameter(name = "period", description = "조회 할 개월수(개월수에 orderDate 가 해당되면 조회) ex.1개월전~현재=1MONTH,3개월전~현재=3MONTH (선택)"),
                    @Parameter(name = "searchDate", description = "조회 할 날짜(선택)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록된 예약이 없습니다."),
            @ApiResponse(responseCode = "200", description = "사용자 예약 조회 성공")
    })
    // 사용자의 예약 전체 조회(주문/배송)
    @GetMapping("/user")
    public ResponseEntity<ResponseMessage> findRentalOrderListByUser(@RequestParam String memberId,
                                                                     @RequestParam(required = false) String period,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<UserOrderViewDTO> orderList = rentalService.findRentalOrderListByUser(memberId,period,searchDate);

        if (orderList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 예약이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("orderList", orderList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "사용자 예약 조회 성공", res));
    }

    // 예약 상세 조회(사용자, 제공자 동일) /{rentalNo}


    @Operation(summary = "예약 전체 조회",
            description = "관리자 예약정보페이지에서 사용",
            parameters = {
                    @Parameter(name = "rentalState", description = "예약 진행상태(선택)"),
                    @Parameter(name = "storeName", description = "회사명(선택)"),
                    @Parameter(name = "categoryName", description = "상위카테고리명 ex.가전/가구(선택)"),
                    @Parameter(name = "searchDate", description = "예약시작날짜(rentalStartDate),만료날짜(rentalEndDate) 사이에 선택 한 날짜가 해당되는 예약만 조회(선택)"),
                    @Parameter(name = "rentalNo", description = "예약번호(선택)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록 된 예약이 없습니다."),
            @ApiResponse(responseCode = "200", description = "예약 전체 조회 성공")
    })
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

        LocalDateTime searchDateTime = null;

        if(searchDate != null) {
            searchDateTime = LocalDate.parse(searchDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .atStartOfDay();
        }

        // 검색 조건 DTO 로 변환
        AdminRentalSearchCriteria criteria = new AdminRentalSearchCriteria(rentalState, storeName, categoryName, searchDateTime, rentalNo);

        List<AdminRentalViewDTO> adminRentalList = rentalService.findRentalAllListByAdmin(criteria);

        if (adminRentalList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록 된 예약이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("adminRentalList", adminRentalList);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "예약 전체 조회 성공", res));
    }



}
