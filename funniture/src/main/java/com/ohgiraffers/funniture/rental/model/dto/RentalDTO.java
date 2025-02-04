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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentalStartDate;   // 대여시작일

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentalEndDate;     // 대여마감일

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;         // 주문일

    private int rentalNumber;       // 대여갯수

    private String rentalState;     // 대여진행상태

    private String deliverCom;      // 택배사

    private String deliveryNo;      // 운송장 번호

    private int rentalInfoNo;       // 대여조건정보 (fk)

    private int destinationNo;      // 배송지 식별번호 (fk)

    private String deliveryMemo;    // 배송메모

    // *************************Join************************

    private MemberDTO memberDTO;    // 제공자, 대여자

    private PostAddressDTO postAddressDTO; // 배송지

    private ProductDTO productDTO;  // 상품

    private RentalOptionInfoDTO rentalOptionInfoDTO; // 대여조건

    private OwnerInfoDTO ownerInfoDTO;  // 제공자 사업정보

    private CategoryDTO categoryDTO;
}
