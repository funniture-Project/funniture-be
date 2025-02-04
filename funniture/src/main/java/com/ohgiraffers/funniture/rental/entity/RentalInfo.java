package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class RentalInfo {

    @Column(name = "rental_number") // 대여 개수
    private int rentalNumber;

    @Column(name = "order_date") // 주문일
    private LocalDateTime orderDate;

    @Column(name = "rental_start_date") // 대여 시작일
    private LocalDateTime rentalStartDate;

    @Column(name = "rental_end_date") // 대여 마감일
    private LocalDateTime rentalEndDate;

    @Column(name = "rental_state") // 대여 진행 상태 (예: 진행중, 완료, 취소)
    private String rentalState;

    @Column(name = "deliver_com") // 택배사
    private String deliverCom;

    @Column(name = "delivery_no") // 운송장번호
    private String deliveryNo;

    @Column(name = "delivery_memo") // 배송 메모
    private String deliveryMemo;

    public RentalInfo() {}

    public RentalInfo(int rentalNumber, LocalDateTime orderDate, LocalDateTime rentalStartDate, LocalDateTime rentalEndDate, String rentalState, String deliverCom, String deliveryNo, String deliveryMemo) {
        this.rentalNumber = rentalNumber;
        this.orderDate = orderDate;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        this.rentalState = rentalState;
        this.deliverCom = deliverCom;
        this.deliveryNo = deliveryNo;
        this.deliveryMemo = deliveryMemo;
    }
}
