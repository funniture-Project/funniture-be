package com.ohgiraffers.funniture.rental.controllers;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PageDTO;
import com.ohgiraffers.funniture.rental.model.dto.*;
import com.ohgiraffers.funniture.rental.model.service.RentalService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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


    /* comment.-------------------------------------------- 사용자 -----------------------------------------------*/

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
                    @Parameter(name = "searchDate", description = "조회 할 날짜(선택)"),
                    @Parameter(name = "offset", description = "현재페이지")
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
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate searchDate,
                                                                     @RequestParam(name = "offset", defaultValue = "1") String offset) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        Criteria cri = new Criteria(Integer.valueOf(offset), 8);

        Page<UserOrderViewDTO> userOrderList = rentalService.findRentalOrderListByUser(memberId,period,searchDate, cri);

        if (userOrderList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 예약이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("userOrderList", userOrderList.getContent());
        res.put("pageInfo", new PageDTO(cri, (int) userOrderList.getTotalElements()));

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "사용자 예약 조회 성공", res));
    }

    @Operation(summary = "사용자의 예약진행상태별 Count",
            description = "사용자 마이페이지에서 사용",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록된 예약이 없습니다."),
            @ApiResponse(responseCode = "200", description = "사용자의 예약진행상태별 count 성공")
    })
    // 사용자의 마이페이지 예약진행상태 카운트
    @GetMapping("count")
    public ResponseEntity<ResponseMessage> countRentalStatesByUser(@RequestParam String memberId) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<RentalStateCountDTO> rentalStateCount = rentalService.countRentalStatesByUser(memberId);

        if (rentalStateCount.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 예약이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("rentalStateCount", rentalStateCount);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "사용자의 예약진행상태별 count 성공", res));
    }

    @Operation(summary = "사용자 사용중인 상품 조회",
            description = "사용자 마이페이지 사용상품/반납에서 사용",
            parameters = {
                    @Parameter(name = "memberId", description = "사용자 ID(필수)"),
                    @Parameter(name = "offset", description = "현재페이지")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록된 예약이 없습니다."),
            @ApiResponse(responseCode = "200", description = "사용자의 사용중인 예약 조회 성공")
    })
    // 사용자 사용중인 상품 조회 = 배송완료상태인 예약
    @GetMapping("/active")
    public ResponseEntity<ResponseMessage> findActiveRentalListByUser(@RequestParam String memberId,
                                                                      @RequestParam(name = "offset", defaultValue = "1") String offset) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        Criteria cri = new Criteria(Integer.valueOf(offset), 8);

        Page<ActiveRentalDTO> activeRentalList = rentalService.findActiveRentalListByUser(memberId, cri);

        if (activeRentalList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 예약이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("activeRentalList", activeRentalList.getContent());
        res.put("pageInfo", new PageDTO(cri, (int) activeRentalList.getTotalElements()));

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "사용자의 사용중인 예약 조회 성공", res));
    }

    @Operation(summary = "예약 상세 조회",
            description = "사용자 마이페이지, 제공자 마이페이지에서 사용",
            parameters = {
                    @Parameter(name = "rentalNo", description = "주문번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404",description = "해당 예약을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "200", description = "예약 상세 조회 성공")
    })
    // 예약 상세 조회(사용자, 제공자 동일) /{rentalNo}
    @GetMapping("/{rentalNo}")
    public ResponseEntity<ResponseMessage> findRentalDetail(@PathVariable String rentalNo){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<RentalDetailDTO> rentalDetail = rentalService.findRentalDetail(rentalNo);

        if (rentalDetail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(new ResponseMessage(404, "해당 예약을 찾을 수 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("rentalDetail", rentalDetail);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "예약 상세 조회 성공", res));
    }

    @Operation(summary = "주문번호별 예약대기,예약완료 상태인 예약 배송지 수정",
            description = "사용자 주문상세페이지에서 사용",
            parameters = {
                    @Parameter(name = "rentalNo", description = "주문번호"),
                    @Parameter(name = "destinationNo", description = "새로운 배송지 식별번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "배송지 수정이 완료되었습니다.")
    })
    // 사용자 예약 배송지 변경
    @PutMapping("/{rentalNo}/deliveryaddress")
    public ResponseEntity<ResponseMessage> updateRentalDeliveryAddress(@PathVariable String rentalNo,
                                                                       @RequestBody String destinationNo) {
        int newDestinationNo = Integer.parseInt(destinationNo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        rentalService.updateRentalDeliveryAddress(rentalNo, newDestinationNo);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "배송지 수정이 완료되었습니다.", null));
    }

    /* comment.-------------------------------------------- 제공자 -----------------------------------------------*/

    @Operation(summary = "제공자별 예약 조회",
            description = "제공자 마이페이지 예약/배송/반납에서 사용",
            parameters = {
                    @Parameter(name = "ownerNo", description = "제공자 ID(필수)"),
                    @Parameter(name = "period", description = "현재날짜로(currentDate)부터 만료일(rental_end_date) 1주일/1개월/3개월 필터링(선택)"),
                    @Parameter(name = "rentalTab", description = "예약/배송/반납 필터링 조회(선택)"),
                    @Parameter(name = "offset", description = "현재페이지")

            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "예약 내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "제공자별 예약 조회 성공")
    })
    @GetMapping("/owner")
    public ResponseEntity<ResponseMessage> findRentalListByOwner(@RequestParam String ownerNo,
                                                                 @RequestParam(required = false) String period,
                                                                 @RequestParam(required = false) String rentalTab,
                                                                 @RequestParam(name = "offset", defaultValue = "1") String offset) {  // offset 추가

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // Criteria 생성 (첫 번째 인자 = 페이지 번호, 두 번째 인자 = 보여 줄 행의 수)
        Criteria cri = new Criteria(Integer.valueOf(offset), 11);

        // Page로 변경하여 페이징된 데이터 가져오기
        Page<OwnerRentalViewDTO> ownerRentalList = rentalService.findRentalListByOwner(ownerNo, period, rentalTab, cri);

        if (ownerRentalList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "예약 내역이 없습니다.", null));
        }

        // 페이징 정보 포함
        Map<String, Object> res = new HashMap<>();
        res.put("ownerRentalList", ownerRentalList.getContent());  // 실제 데이터 (content) 가져오기
        res.put("pageInfo", new PageDTO(cri, (int) ownerRentalList.getTotalElements()));  // PageDTO 정보 포함

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "제공자별 예약 조회 성공", res));
    }

    @Operation(summary = "다중선택하여 예약대기에서 예약완료 상태 업데이트",
            description = "제공자 예약/배송/반납페이지에서 사용",
            parameters = {
            @Parameter(name = "rentalNos", description = "다중선택 한 주문번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "선택된 예약들이 예약완료로 상태 변경되었습니다."),
            @ApiResponse(responseCode = "500", description = "예약 확정 중 오류가 발생했습니다.")
    })
    @PutMapping("/confirmBatch")
    public ResponseEntity<ResponseMessage> confirmRentals(@RequestParam List<String> rentalNos) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        try {
            rentalService.confirmRentals(rentalNos);  // 여러 개의 예약번호 처리
            return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "선택된 예약들이 예약완료로 상태 변경되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(new ResponseMessage(500, "예약 확정 중 오류가 발생했습니다.", null));
        }

    }

    @Operation(summary = "예약대기에서 예약취소 상태 업데이트",
            description = "제공자 마이페이지 주문 상세모달에서 사용",
            parameters = {
                    @Parameter(name = "rentalNo", description = "주문번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약취소로 상태 변경되었습니다."),
            @ApiResponse(responseCode = "500", description = "예약 취소 오류가 발생했습니다.")
    })
    @PutMapping("/cancel")
    public ResponseEntity<ResponseMessage> cancelBatch(@RequestParam String rentalNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        String rentalState = "예약취소";

        try {
            rentalService.cancelBatch(rentalNo, rentalState);
            return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "예약취소로 상태 변경되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(new ResponseMessage(500, "예약 취소 오류가 발생했습니다.", null));
        }

    }

    @Operation(summary = "운송장 번호와 운송업체명 수정",
            description = "제공자 예약/배송/반납페이지에서 사용",
            parameters = {
                    @Parameter(name = "rentalNo", description = "주문번호"),
                    @Parameter(name = "deliveryNo", description = "운송장 번호"),
                    @Parameter(name = "deliverCom", description = "운송업체명")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "운송장 번호와 운송업체명이 성공적으로 수정되었습니다."),
            @ApiResponse(responseCode = "500", description = "운송장 번호 또는 운송업체명 수정 중 오류가 발생했습니다.")
    })
    @PutMapping("/{rentalNo}/delivery")
    public ResponseEntity<ResponseMessage> updateDelivery(@PathVariable String rentalNo,
                                                          @RequestParam String deliveryNo,
                                                          @RequestParam String deliverCom) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        try {
            rentalService.updateDelivery(rentalNo, deliveryNo, deliverCom);  // 운송장 번호와 업체명 업데이트
            return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "운송장 번호와 운송업체명이 성공적으로 수정되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(new ResponseMessage(500, "운송장 번호 또는 운송업체명 수정 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "예약진행상태 수정",
            description = "제공자 예약/배송/반납페이지에서 사용",
            parameters = {
                    @Parameter(name = "rentalNo", description = "주문번호"),
                    @Parameter(name = "newState", description = "변경 할 예약진행상태")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 상태가 성공적으로 업데이트되었습니다."),
            @ApiResponse(responseCode = "500", description = "예약 상태 업데이트 중 오류가 발생했습니다.")
    })
    // 예약진행상태 수정 (배송중 -> 배송완료, 수거중 -> 수거완료)
    @PutMapping("/{rentalNo}/state")
    public ResponseEntity<ResponseMessage> updateRentalState(@PathVariable String rentalNo) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        try {
            rentalService.updateRentalState(rentalNo);
            return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "예약 상태가 성공적으로 업데이트되었습니다.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .headers(headers)
                    .body(new ResponseMessage(500, "예약 상태 업데이트 중 오류가 발생했습니다.", null));
        }
    }

    @Operation(summary = "제공자별 매출 현황 조회",
            description = "제공자 마이페이지 정산관리에서 사용",
            parameters = {
                    @Parameter(name = "ownerNo", description = "제공자 회원번호(필수)"),
                    @Parameter(name = "yearMonth", description = "월별 필터링(필수)"),
                    @Parameter(name = "productNo", description = "주문번호 필터링(선텍)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "매출내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "제공자의 월별 매출내역 조회 성공")
    })
    // 제공자별 매출 현황 조회
    @GetMapping("/{ownerNo}/sales")
    public ResponseEntity<ResponseMessage> getSalesByOwner(@PathVariable String ownerNo,
                                                           @RequestParam String yearMonth,
                                                           @RequestParam(required = false) String productNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<OwnerSalesDTO> ownerSalesData = rentalService.getSalesByOwner(ownerNo, yearMonth, productNo);

        if (ownerSalesData.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "월별 매출내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("ownerSalesData", ownerSalesData);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "제공자의 월별 매출내역 조회 성공", res));
    }

    @Operation(summary = "제공자별 이번 달 매출 조회(상품별 합산)",
            description = "제공자 마이페이지 메인페이지에서 차트의 데이터로 사용",
            parameters = {
                    @Parameter(name = "ownerNo", description = "제공자 회원번호(필수)"),
                    @Parameter(name = "yearMonth", description = "월별 필터링(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "매출내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "제공자의 이번 달 매출 조회 성공")
    })
    // 이번 달 매출 API
    @GetMapping("/{ownerNo}/chart/currentMonth")
    public ResponseEntity<ResponseMessage> getCurrentMonthSales(@PathVariable String ownerNo,
                                                                @RequestParam String yearMonth) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<CurrentMonthSalesDTO> currentSalesData = rentalService.getCurrentMonthSales(ownerNo, yearMonth);

        if (currentSalesData.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "매출내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("currentSalesData", currentSalesData);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "제공자의 이번 달 매출 조회 성공", res));
    }

    @Operation(summary = "제공자별 월별 매출 조회(상품전체 합산)",
            description = "제공자 마이페이지 메인페이지에서 차트의 데이터로 사용",
            parameters = {
                    @Parameter(name = "ownerNo", description = "제공자 회원번호(필수)"),
                    @Parameter(name = "yearMonth", description = "월별 필터링(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "매출내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "제공자의 월별 매출 조회 성공")
    })
    // 월별 매출 API
    @GetMapping("/{ownerNo}/chart/monthly")
    public ResponseEntity<ResponseMessage> getMonthlySales(@PathVariable String ownerNo,
                                                           @RequestParam String yearMonth) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        // 월별 매출 데이터 조회
        List<MonthlySalesDTO> monthlySalesData = rentalService.getMonthlySales(ownerNo, yearMonth);

        // 매출 데이터가 없는 경우 처리
        if (monthlySalesData.isEmpty()) {
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "매출내역이 없습니다.", null));
        }

        // 응답 데이터 구성
        Map<String, Object> res = new HashMap<>();
        res.put("monthlySalesData", monthlySalesData);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "제공자의 월별 매출 조회 성공", res));
    }

    @Operation(summary = "제공자의 예약진행상태별 Count",
            description = "제공자 마이페이지의 메인에서 사용",
            parameters = {
                    @Parameter(name = "ownerNo", description = "제공자 ID(필수)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록된 예약이 없습니다."),
            @ApiResponse(responseCode = "200", description = "제공자의 예약진행상태별 count 성공")
    })
    // 제공자의 마이페이지 예약진행상태 카운트
    @GetMapping("/{ownerNo}/count")
    public ResponseEntity<ResponseMessage> countRentalStatesByOwner(@PathVariable String ownerNo) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<RentalStateCountDTO> rentalStateCount = rentalService.countRentalStatesByOwner(ownerNo);

        if (rentalStateCount.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록된 예약이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("rentalStateCount", rentalStateCount);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "제공자의 예약진행상태별 count 성공", res));
    }


    /* comment.-------------------------------------------- 관리자 -----------------------------------------------*/

    @Operation(summary = "예약 전체 조회",
            description = "관리자 예약정보페이지에서 사용",
            parameters = {
                    @Parameter(name = "rentalState", description = "예약 진행상태(선택)"),
                    @Parameter(name = "storeName", description = "회사명(선택)"),
                    @Parameter(name = "categoryName", description = "상위카테고리명 ex.가전/가구(선택)"),
                    @Parameter(name = "searchDate", description = "예약시작날짜(rentalStartDate),만료날짜(rentalEndDate) 사이에 선택 한 날짜가 해당되는 예약만 조회(선택)"),
                    @Parameter(name = "rentalNo", description = "예약번호(선택)"),
                    @Parameter(name = "offset", description = "현재페이지")
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
                                                                    @RequestParam(required = false) String rentalNo,
                                                                    @RequestParam(name = "offset", defaultValue = "1") String offset

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

        Criteria cri = new Criteria(Integer.valueOf(offset), 11);

        Page<AdminRentalViewDTO> adminRentalList = rentalService.findRentalAllListByAdmin(criteria, cri);

        if (adminRentalList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "예약 내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("adminRentalList", adminRentalList.getContent());
        res.put("pageInfo", new PageDTO(cri, (int) adminRentalList.getTotalElements()));

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "예약 전체 조회 성공", res));
    }


    @Operation(summary = "매출 현황 조회",
            description = "관리자 마이페이지 매출현황에서 사용",
            parameters = {
                    @Parameter(name = "yearMonth", description = "월별 매출현황(필수)"),
                    @Parameter(name = "storeName", description = "회사별 매출 필터링(선택)"),
                    @Parameter(name = "offset", description = "현재페이지")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "매출내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "월별 매출내역 조회 성공")
    })
    // 관리자 - 매출 현황 조회
    @GetMapping("/admin/sales")
    public ResponseEntity<ResponseMessage> getSalesByDate(@RequestParam(value ="yearMonth") String yearMonth,
                                                    @RequestParam(required = false) String storeName,
                                                    @RequestParam(name = "offset", defaultValue = "1") String offset) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        Criteria cri = new Criteria(Integer.valueOf(offset), 8);

        Page<AdminSalesDTO> salesData = rentalService.getSalesByDate(yearMonth, storeName, cri);

        if (salesData.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "월별 매출내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("salesData", salesData.getContent());
        res.put("pageInfo", new PageDTO(cri, (int) salesData.getTotalElements()));

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "월별 매출내역 조회 성공", res));
    }

    @Operation(summary = "월별/일별 매출 차트",
            description = "관리자 마이페이지 메인에서 사용",
            parameters = {
                    @Parameter(name = "yearMonth", description = "날짜 데이터"),
                    @Parameter(name = "groupBy", description = "일별/월별 구분(day/month)")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "매출내역이 없습니다."),
            @ApiResponse(responseCode = "200", description = "매출내역 합산 조회 성공")
    })
    // 관리자 - 매출 합산 조회(차트)
    @GetMapping("/chart")
    public ResponseEntity<ResponseMessage> getSales(@RequestParam String yearMonth,
                                                    @RequestParam String groupBy) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("Application", "json", Charset.forName("UTF-8")));

        List<AdminMonthlySalesDTO> sales = rentalService.getSales(yearMonth, groupBy);

        if (sales.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "매출내역이 없습니다.", null));
        }

        Map<String, Object> res = new HashMap<>();
        res.put("sales", sales);

        return ResponseEntity.ok().headers(headers).body(new ResponseMessage(200, "매출내역 합산 조회 성공", res));
    }


}