package com.ohgiraffers.funniture.rental.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RentalDetailDTO {

    private String rentalNo;        // 예약번호 (pk)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;         // 주문일

    private int rentalNumber;       // 대여갯수

    private String rentalState;     // 대여진행상태

    private String deliveryMemo;    // 배송메모

    private String storeName;       // 회사명

    private String productName;     // 상품명

    private String productImageLink;    // 이미지 링크

    private int rentalPrice;        // 렌탈 가격

    private int rentalTerm;         // 렌탈 개월수

    private int asNumber;           // A/S 갯수

    private String destinationName;     // 배송지 이름

    private String destinationPhone;    // 배송지 전화번호

    private String destinationAddress;  // 주소

    private String receiver;            // 받는분

    private String email;        // 대여자 이메일

    private String userName;        // 대여자 이름

    private String phoneNumber;        // 대여자 폰번호




}
