package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminRentalViewDTO {

    private String rentalNo;        // 예약번호 (pk)

    private String storeName;       // 사업장 이름

    private String rentalState;     // 대여진행상태

    private LocalDateTime rentalStartDate;   // 대여시작일

    private LocalDateTime rentalEndDate;     // 대여마감일

    private int rentalNumber;       // 대여갯수

    private String categoryName;

    private String productName;      // 상품명
}
