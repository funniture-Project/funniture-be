package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Entity(name = "rental")
@Table(name = "tbl_rental")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RentalEntity {
    


    @Id
    @Column(name = "rental_no")
    private String rentalNo;        // 예약번호 (pk)

    @Embedded
    private RentalInfo rentalInfo; // 대여 정보 포함

    @Column(name = "member_id")
    private String memberId;        // 대여자 회원번호 (fk)

    @Column(name = "owner_no")
    private String ownerNo;         // 제공자 회원번호 (fk)

    @Column(name = "rental_info_no")
    private int rentalInfoNo;       // 대여조건정보 (fk)

    @Column(name = "destination_no")
    private int destinationNo;      // 배송지 식별번호 (fk)

    @Column(name = "product_no")
    private String productNo;       // 상품번호 (fk)

    //    @ManyToOne
//    @JoinColumn(name = "renter_id") // 대여자 (회원)
//    private User renter;
//
//    @ManyToOne
//    @JoinColumn(name = "provider_id") // 제공자 (회원)
//    private User provider;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id") // 상품
//    private Product product;
//
//    @ManyToOne
//    @JoinColumn(name = "rental_condition_id") // 대여 조건
//    private RentalCondition rentalCondition;
//
//    @ManyToOne
//    @JoinColumn(name = "shipping_address_id") // 배송지
//    private Address shippingAddress;

}
