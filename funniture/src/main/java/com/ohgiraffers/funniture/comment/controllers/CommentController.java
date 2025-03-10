package com.ohgiraffers.funniture.comment.controllers;

import com.ohgiraffers.funniture.comment.model.dto.CommentRegistDTO;
import com.ohgiraffers.funniture.comment.model.service.CommentService;
import com.ohgiraffers.funniture.inquiry.model.dto.InquiryDTO;
import com.ohgiraffers.funniture.response.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

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

}
