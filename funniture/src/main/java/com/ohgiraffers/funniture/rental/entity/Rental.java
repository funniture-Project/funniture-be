package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class Rental {

    @Id
    @Column(name = "rental_no")
    private String rentalNo;        // 예약번호 (pk)

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

    @Column(name = "rental_number")
    private int rentalNumber;       // 대여갯수

    @Column(name = "order_date")
    private Date orderDate;         // 주문일

    @Column(name = "rental_start_date")
    private Date rentalStartDate;   // 대여시작일

    @Column(name = "rental_end_date")
    private Date rentalEndDate;     // 대여마감일

    @Column(name="rental_state")
    private String rentalState;     // 대여진행상태

    @Column(name = "deliver_com")
    private String deliverCom;      // 택배사

    @Column(name = "delivery_no")
    private String deliveryNo;      // 운송장번호

    @Column(name = "delivery_memo")
    private String deliveryMemo;    // 배송메모


}
