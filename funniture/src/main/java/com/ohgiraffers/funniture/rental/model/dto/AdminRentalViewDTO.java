package com.ohgiraffers.funniture.rental.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String categoryName;    // 카테고리명

    private String productName;     // 상품명

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rentalStartDate;   // 대여시작일

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime rentalEndDate;     // 대여마감일

    private int rentalNumber;       // 대여갯수
    
    private String  memberId;       // 대여자
    
    private String ownerNo;         // 제공자

}
