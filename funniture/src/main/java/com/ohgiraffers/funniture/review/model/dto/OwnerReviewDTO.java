package com.ohgiraffers.funniture.review.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OwnerReviewDTO {

    private String reviewNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewWriteTime;

    private String reviewContent;
    private String memberId;
    private String productNo;
    private float score;

    private String productName;
    private String productImageLink;
    private String userName;

    private int rentalTerm;
}
