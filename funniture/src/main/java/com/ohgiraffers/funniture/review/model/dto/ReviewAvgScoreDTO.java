package com.ohgiraffers.funniture.review.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAvgScoreDTO {

//    private String reviewNo;

//    private String reviewContent;
//    private String memberId;
    private String productNo;
    private double score; // 캐스팅 에러 때문에 double로

    private String productName;
}
