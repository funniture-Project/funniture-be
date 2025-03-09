package com.ohgiraffers.funniture.review.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class WritableReviewDTO {
    private String rentalNo; // 대여 번호

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate; // 주문 날짜
    private String productNo; // 상품 번호
    private String productName; // 상품 이름
    private String productImageLink; // 상품 이미지 링크
    private int rentalTerm; // 대여 기간
    private int rentalPrice; // 대여 가격
}
