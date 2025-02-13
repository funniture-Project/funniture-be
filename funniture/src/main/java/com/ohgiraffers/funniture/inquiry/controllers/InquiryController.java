package com.ohgiraffers.funniture.inquiry.controllers;

import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.OwnerInquiryDTO;
import com.ohgiraffers.funniture.inquiry.model.service.InquiryService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Inquiry API")
@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @Operation(summary = "문의 조회")

    private HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

    // 문의 전체 조회
//    @GetMapping
//    public ResponseEntity<ResponseMessage> findAllInquiry (){
//
//        List<InquiryDTO> result = inquiryService.findAllInquiry();
//
//        Map<String , Object> map = new HashMap<>();
//        map.put("result" , result);
//
//        return ResponseEntity.ok()
//                .headers(headersMethod())
//                .body(new ResponseMessage(200 , "조회 성공",map));
//    }


    @Operation(summary = "문의 조회",
            description = "상세 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 조회 성공")
    })
    // 상세 페이지에 해당 상품에 대한 전체 문의
    @GetMapping ("/product/{productNo}")
    public ResponseEntity<ResponseMessage> findByProductNo (@PathVariable String productNo){
        System.out.println("컨트롤러 productNo = " + productNo);
        List<InquiryDTO> result = inquiryService.findByProductNo(productNo);

        Map <String , Object> map = new HashMap<>();
        map.put("map", result);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "조회 성공", map));
    }


    @Operation(summary = "문의 번호로 조회",
            description = "제공자 마이 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 번호로 조회 성공")
    })
    // 문의 번호로 조회
    @GetMapping("/{inquiryNo}")
    public ResponseEntity<ResponseMessage> findByInquiryNo(@PathVariable String inquiryNo){

        InquiryDTO inquiry = inquiryService.findByInqiryNo(inquiryNo);

        Map <String , Object> map = new HashMap<>();
        map.put("map", inquiry);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "조회 성공", map));
    }


    @Operation(summary = "문의 등록",
            description = "상세 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "문의 등록 성공")
    })
    // 문의 등록
    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> inquiryRegist(@RequestBody InquiryDTO inquiryDTO){

        System.out.println("살인마 json에서 들어온 inquiryDTO = " + inquiryDTO);

        String maxInquiry = inquiryService.getMaxInquiry();
        System.out.println("유어 마인드 컨트롤러 maxInquiry = " + maxInquiry);

        String newNo = returnInquiryNo(maxInquiry);
        inquiryDTO.setInquiryNo(newNo);

//        inquiryService.inquiryRegist(inquiryDTO);

        Map<String , Object> map = new HashMap<>();
        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(201, "등록 성공", map));
    }

    public String returnInquiryNo(String maxInquiry){
        if (maxInquiry == null || maxInquiry.isEmpty()){
            return "INQ001";
        } else {
            int newInquiryNo = Integer.parseInt(maxInquiry.substring(3)) + 1;
            System.out.println("newInquiryNo = " + newInquiryNo);
            return String.format("INQ%03d",newInquiryNo);
        }
    }


    @Operation(summary = "문의 삭제",
            description = "사용자 마이 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "문의 삭제 성공")
    })
    // 문의 번호로 삭제하기
    @DeleteMapping("/delete/{inquiryNo}")
    public ResponseEntity<ResponseMessage> inquiryDelete(@PathVariable String inquiryNo){
        System.out.println("화면에서 inquiryNo 잘 받아오나 = " + inquiryNo);

        inquiryService.deleteByInquiryNo(inquiryNo);

        Map<String , Object> map = new HashMap<>();

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(201, "삭제 성공", map));
    }



    @Operation(summary = "제공자 페이지 전체 문의 조회",
            description = "제공자 마이 페이지에서 문의 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 조회 성공")
    })
    // member_id에 따른 제공자 페이지의 전체 문의들
    @GetMapping("/owner/{ownerNo}")
    public ResponseEntity<ResponseMessage> findAllOwnerPageInquiry (@PathVariable String ownerNo) {

        System.out.println("프론트에서 memberId 잘 받아오는지 = " + ownerNo);
        List<OwnerInquiryDTO> result = inquiryService.findByInquiryOwnerPage(ownerNo);

        System.out.println("서비스에서 넘어온 result = " + result);
        Map <String , Object> map = new HashMap<>();
        map.put("result", result);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "조회 성공", map));
    }


    @Operation(summary = "제공자 페이지 문의 답변 대기",
            description = "제공자 마이 페이지에서 문의 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답변 대기 문의 조회 성공")
    })
    // 답변하지 않은 문의들
    @GetMapping ("/wait")
    public ResponseEntity<ResponseMessage> findWaitOwnerPageInquiry () {

        return null;
    }


    @Operation(summary = "제공자 페이지 문의 답변 완료",
            description = "제공자 마이 페이지에서 문의 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "답변 완료 문의 조회 성공")
    })
    @GetMapping ("/complete")
    // 답변하지 않은 문의들
    public ResponseEntity<ResponseMessage> findCompleteOwnerPageInquiry () {

        return null;
    }


    @Operation(summary = "제공자 페이지 문의 답변 등록",
            description = "제공자 마이 페이지에서 문의 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "문의 답변 성공")
    })
    // 문의 답변 하기
    @PutMapping("/answer/{inquiryNo}")
    public ResponseEntity<ResponseMessage> inquiryAnswer(@PathVariable String inquiryNo
            ,@RequestBody OwnerInquiryDTO ownerInquiryDTO){

        return null;
    }


    @Operation(summary = "제공자 페이지 문의 답변 수정",
            description = "제공자 마이 페이지에서 문의 페이지에서 사용"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "문의 답변 수정 성공")
    })
    // 문의 답변 수정하기
    @PutMapping("/modify/{inquiryNo}")
    public ResponseEntity<ResponseMessage> inquiryModify(@PathVariable String inquiryNo
            ,@RequestBody OwnerInquiryDTO ownerInquiryDTO){

        System.out.println("컨트롤러 : 화면에서 inquiryNo 받아오나 = " + inquiryNo);

        inquiryService.modifyByInquiryNo(inquiryNo, ownerInquiryDTO);

        Map<String , Object> map = new HashMap<>();

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(201, "수정 성공", map));
    }

}
