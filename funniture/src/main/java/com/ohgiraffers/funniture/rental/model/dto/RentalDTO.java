package com.ohgiraffers.funniture.rental.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RentalDTO {

    private String rentalNo;        // 예약번호 (pk)

    private String memberId;        // 대여자 회원번호 (fk)

    private String ownerNo;         // 제공자 회원번호 (fk)

    private String productNo;       // 상품번호 (fk)

    private LocalDateTime orderDate;         // 주문일

    private int rentalNumber;       // 대여갯수

    private String rentalState;     // 대여진행상태

    private int rentalInfoNo;       // 대여조건정보 (fk)

    private int destinationNo;      // 배송지 식별번호 (fk)

    private String deliveryMemo;    // 배송메모
}
