package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostAddressDTO {

    private int destinationNo;        // 배송지 식별번호 (pk)

    private String memberId;          // 대여자 회원번호 (fk)

    private String destinationName;   // 배송지 이름

    private String destinationPhone;  // 배송지 전화번호

    private String receiver;          // 받는이

    private String destinationAddress; // 배송지주소


}
