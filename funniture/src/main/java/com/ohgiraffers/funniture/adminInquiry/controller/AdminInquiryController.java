package com.ohgiraffers.funniture.adminInquiry.controller;

import com.ohgiraffers.funniture.adminInquiry.model.dto.AdminInquiryDTO;
import com.ohgiraffers.funniture.adminInquiry.model.dto.ConsultingListDTO;
import com.ohgiraffers.funniture.adminInquiry.model.service.AdminInquiryService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "ADMIN INQUIRY API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/adinquiry")
public class AdminInquiryController {

    private final AdminInquiryService adService;

    // 사용자 별 문의 내역 불러오기
    @Operation(summary = "사용자 별 문의 내역 불러오기",
            description = "사용자별 문의 내역 및 답변받은 내역을 받아오며 사용자가 보낸것과 받은것을 구분지어 반환",
            parameters = {
                    @Parameter(name = "memberId", description = "조회할 회원 번호")
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "조회 내용 없음"),
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseMessage> getListById(@PathVariable String memberId){
        List<AdminInquiryDTO> selectResult = adService.getListById(memberId);

        // 사용자가 보낸 데이터
        List<AdminInquiryDTO> userSendList = selectResult.stream().filter(item -> item.getSenderNo().equals(memberId))
                                                .collect(Collectors.toList());
        // 관리자가 보낸 데이터
        List<AdminInquiryDTO> adminSendList = selectResult.stream().filter(item -> item.getReceiveNo().equals(memberId))
                                                .collect(Collectors.toList());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (userSendList.isEmpty() && adminSendList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "문의 기록 없음",null));
        }

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("userSendList",userSendList);
        responseMap.put("adminSendList",adminSendList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "문의 내역 조회 성공",responseMap));
    }

    // 문의중인 사용자 명단 가져오기
    @Operation(summary = "문의 기록이 있는 사용자 목록",
            description = "문의 기록이 있는 사람들의 정보 (최근 순서로)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "문의자 없음"),
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getConsultingList(){
        List<ConsultingListDTO> consultingList = adService.getConsultingList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (consultingList.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "1:1문의 없음",null));
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("result", consultingList);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "문의자 조회 완료",responseMap));
    }

}
