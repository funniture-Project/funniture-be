package com.ohgiraffers.funniture.review.controllers;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.PagingResponseDTO;
import com.ohgiraffers.funniture.member.controller.AuthController;
import com.ohgiraffers.funniture.response.ResponseMessage;
import com.ohgiraffers.funniture.review.model.dto.ReviewRegistDTO;
import com.ohgiraffers.funniture.review.model.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Operation(summary = "리뷰 조회",
            description = "사용자 페이지 리뷰 조회",
            parameters = {
                    @Parameter(name = "ownerNo", description = "사용자 번호로 전체 리뷰 조회"),
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 조회 성공"),
            @ApiResponse(responseCode = "404", description = "리뷰 조회 실패")
    })
    // member_id에 따른 사용자 마이 페이지의 전체 리뷰들
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ResponseMessage> findAllUserPageReview(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @PathVariable String memberId) {

        System.out.println("프론트에서 memberId 잘 받아오는지 = " + memberId);
        System.out.println("프론트에서 잘 넘어 왔는지 page = " + page);
        System.out.println("프론트에서 잘 넘어 왔는지 size = " + size);

        Criteria cri = new Criteria(page, size);
        PagingResponseDTO pagingResponseDTO = reviewService.findByReviewUserPage(memberId, cri);

        System.out.println("서비스에서 리뷰랑 페이지 정보 잘 넘어 왔는지 pagingResponseDTO = " + pagingResponseDTO);

        Map<String , Object> response = new HashMap<>();
        response.put("result", pagingResponseDTO);

        if (((List<?>) pagingResponseDTO.getData()).isEmpty()) {
            return ResponseEntity.ok()
                    .headers(authController.headersMethod())
                    .body(new ResponseMessage(404, "등록된 리뷰가 없습니다.", null));
        }

        return ResponseEntity.ok()
                .headers(authController.headersMethod())
                .body(new ResponseMessage(200, "리뷰 조회 성공", response));
    }
}
