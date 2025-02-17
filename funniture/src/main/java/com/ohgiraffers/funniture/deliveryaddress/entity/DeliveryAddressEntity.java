package com.ohgiraffers.funniture.deliveryaddress.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tbl_postaddress")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class DeliveryAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_no")
    private int destinationNo;          // 배송지번호(PK)

    @Column(name = "member_id")
    private String memberId;            // 회원번호(FK)

    @Column(name = "destination_name")
    private String destinationName;     // 배송지 이름

    @Column(name = "destination_phone")
    private String destinationPhone;    // 배송지 전화번호


    @Column(name = "destination_address")
    private String destinationAddress;  // 배송지

    @Column(name = "receiver")
    private String receiver;            // 받는 이

    @Column(name = "is_default")
    private boolean isDefault;          // 기본배송지 여부
}
