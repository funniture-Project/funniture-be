package com.ohgiraffers.funniture.deliveryaddress.model.dto;

import jakarta.persistence.Entity;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DeliveryAddressDTO {

    private int destinationNo;          // 배송지번호(PK)

    private String memberId;            // 회원번호(FK)

    private String destinationName;     // 배송지 이름

    private String destinationPhone;    // 배송지 전화번호

    private String destinationAddress;  // 배송지

    private String receiver;            // 받는 이

    private boolean isDefault;          // 기본배송지 여부


}
