package com.ohgiraffers.funniture.rental.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ActiveRentalDTO {

    private String rentalNo;        // 예약번호 (pk)

    private int rentalNumber;       // 대여갯수

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rentalStartDate;  // 대여 시작일

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rentalEndDate;    // 대여 마감일

    private String productName;     // 상품명

    private int rentalTerm;         // 렌탈 개월수

    private int asNumber;           // A/S 갯수

    private Integer rentalPrice;           // 렌탈 가격

    private String productImageLink;           // 상품 이미지


}
