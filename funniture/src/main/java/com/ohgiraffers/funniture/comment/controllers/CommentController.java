package com.ohgiraffers.funniture.comment.controllers;

import com.ohgiraffers.funniture.comment.model.dto.CommentByMyPageDTO;
import com.ohgiraffers.funniture.comment.model.dto.CommentByProductDTO;
import com.ohgiraffers.funniture.comment.model.dto.CommentRegistDTO;
import com.ohgiraffers.funniture.comment.model.service.CommentService;
import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PagingResponseDTO;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
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
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "COMMENT API")
@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private HttpHeaders headersMethod () {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return headers;
    }

    @Operation(summary = "문의 답변 등록)",
            description = "제공자 페이지에서 문의 답변"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "문의 답변 성공")
    })
    // 문의 답변 등록
    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> inquiryAnswerRegist(@RequestBody CommentRegistDTO commentRegistDTO){
        // DB에서 해당 문의에 대한 마지막 댓글 번호를 가져옴
        int maxComment = commentService.getMaxComment();
        System.out.println("maxComment = " + maxComment);

        // 새로운 댓글 번호 생성
        int newCommentNo = returnCommentNo(maxComment);
        System.out.println("생성된 newCommentNo = " + newCommentNo);

        // commentLevel 설정
        commentRegistDTO.setCommentLevel(1); // 최상위 댓글로 간주

        // CommentRegistDTO에 데이터 세팅
        commentRegistDTO.setCommentNo(newCommentNo);
        commentRegistDTO.setCommentWriteTime(LocalDateTime.now());

        // DB에 저장
        commentService.commentRegist(commentRegistDTO);

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(204, "답변 등록 성공", null));
    }

    // 수정
    public int returnCommentNo(int maxComment){
        if (maxComment <= 0){
            return 1;
        } else {
            int newCommentNo = maxComment + 1;
            System.out.println("newCommentNo = " + newCommentNo);
            return newCommentNo;
        }
    }

    @Operation(summary = "문의 답변 조회",
            description = "사용자 페이지 문의 답변 조회",
            parameters = {
                    @Parameter(name = "inquiryNo", description = "문의 번호로 개별 답변 조회"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "문의 답변 조회 성공"),
            @ApiResponse(responseCode = "404", description = "문의 답변 조회 실패")
    })
    // inquiryNo에 따른 개별 문의 답변 조회(일단 대댓글은 안 하고 답글만 할 거기 때문에 이렇게 조회)
    @GetMapping("/inquiry/{inquiryNo}")
    public ResponseEntity<ResponseMessage> findCommentUserPageInquiry(@PathVariable String inquiryNo) {

        System.out.println("프론트에서 inquiryNo 잘 받아오는지 = " + inquiryNo);

        CommentByMyPageDTO result = commentService.findByInquiryComment(inquiryNo);
        Map<String, Object> map = new HashMap<>();
        map.put("map", result);

        if (result == null) {
            return ResponseEntity.ok()
                    .headers(headersMethod())
                    .body(new ResponseMessage(404, "등록된 문의 답변이 없습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "문의 답변 조회에 성공하였습니다.", map));
    }

    @Operation(summary = "문의 답변 조회",
            description = "상세페이지 문의 답변 조회",
            parameters = {
                    @Parameter(name = "inquiryNo", description = "문의 번호로 개별 답변 조회"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세페이지 문의 답변 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상세페이지 문의 답변 조회 실패")
    })
    // inquiryNo에 따른 상세페이지 개별 문의 답변 조회(일단 대댓글은 안 하고 답글만 할 거기 때문에 이렇게 조회)
    @GetMapping("/product/{inquiryNo}")
    public ResponseEntity<ResponseMessage> findCommentProductPageInquiry(@PathVariable String inquiryNo) {

        System.out.println("프론트에서 inquiryNo 잘 받아오는지 = " + inquiryNo);

        CommentByProductDTO result = commentService.findCommentByProduct(inquiryNo);
        Map<String, Object> map = new HashMap<>();
        map.put("map", result);

        if (result == null) {
            return ResponseEntity.ok()
                    .headers(headersMethod())
                    .body(new ResponseMessage(404, "상세페이지 등록된 문의 답변이 없습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(headersMethod())
                .body(new ResponseMessage(200, "상세페이지 문의 답변 조회에 성공하였습니다.", map));
    }
}
