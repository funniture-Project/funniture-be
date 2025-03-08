package com.ohgiraffers.funniture.review.controllers;

import com.ohgiraffers.funniture.member.controller.AuthController;
import com.ohgiraffers.funniture.response.ResponseMessage;
import com.ohgiraffers.funniture.review.model.dto.ReviewRegistDTO;
import com.ohgiraffers.funniture.review.model.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Review API")
@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthController authController;

    @Operation(summary = "리뷰 등록)",
            description = "상세페이지에 리뷰 등록"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "리뷰 등록 성공")
    })
    // 문의 등록
    @PostMapping("/regist")
    public ResponseEntity<ResponseMessage> reviewRegist(@RequestBody ReviewRegistDTO reviewRegistDTO){

        System.out.println("프런트에서 들어온 reviewRegistDTO = " + reviewRegistDTO);

        String maxReview = reviewService.getMaxReview();
        System.out.println("서비스 갔다온 maxReview = " + maxReview);

        String newNo = returnReviewNo(maxReview);
        reviewRegistDTO.setReviewNo(newNo);

        reviewService.ReviewRegist(reviewRegistDTO);

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(204, "리뷰 등록 성공", null));
    }

    public String returnReviewNo(String maxReview){
        if (maxReview == null || maxReview.isEmpty()){
            return "REV001";
        } else {
            int newReviewNo = Integer.parseInt(maxReview.substring(3)) + 1;
            System.out.println("newReviewNo = " + newReviewNo);
            return String.format("REV%03d",newReviewNo);
        }
    }
}
