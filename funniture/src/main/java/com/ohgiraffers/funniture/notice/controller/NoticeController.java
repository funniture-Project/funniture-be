package com.ohgiraffers.funniture.notice.controller;

import com.ohgiraffers.funniture.notice.model.dto.NoticeDTO;
import com.ohgiraffers.funniture.notice.model.service.NoticeService;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Notice API")
@RequestMapping("/api/v1/notice/")
@RestController
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final ConversionService conversionService;

    @Operation(summary = "전체 공지사항 리스트",
            description = "전체 공지사항 리스트 반환하기"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "조회 내용 없음"),
            @ApiResponse(responseCode = "200", description = "조회 성공"),
    })
    @GetMapping("/list")
    public ResponseEntity<ResponseMessage> getAllNoticeList(){
        List<NoticeDTO> result =  noticeService.getAllNoticeList();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (result.isEmpty()){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "공지사항 없음",null ));
        }

        Map<String, Object> responseMap = new HashMap<>();

        responseMap.put("result",result);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ResponseMessage(200, "공지사항 조회 성공",responseMap));
    }

    @Operation(summary = "공지사항 등록",
            description = "공지사항 등록하기"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "등록 성공"),
            @ApiResponse(responseCode = "500",description = "등록 실패")
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerNotice(@RequestBody NoticeDTO notice){
        System.out.println("받은 notice = " + notice);

        boolean result = noticeService.registerNotice(notice);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (result){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "등록 성공",null));
        } else{
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(500, "등록 실패",null));
        }
    }

    @Operation(summary = "공지사항 삭제",
            description = "공지사항 삭제하기"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204",description = "삭제 성공"),
            @ApiResponse(responseCode = "500",description = "삭제 실패")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseMessage> deleteNotice(@RequestParam String noticeNo){
        System.out.println("받은 noticeNo = " + noticeNo);

        boolean response =  noticeService.deleteNotice(noticeNo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        if (response){
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(204, "삭제 성공",null));
        } else{
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ResponseMessage(500, "삭제 실패",null));
        }
    }

}
