package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RentalOptionInfoDTO {

    private int rentalInfoNo;   // 대여조건 식별자 (pk)

    private String productNo;   // 상품번호 (fk)

    private int rentalTerm;     // 대여기간

    private int rentalPrice;    // 가격

    private int asNumber;       // A/S 횟수
}
