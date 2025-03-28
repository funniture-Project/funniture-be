package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity(name = "rental")
@Table(name = "tbl_rental")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class RentalEntity {

    @Id
    @Column(name = "rental_no")
    private String rentalNo;        // 예약번호 (pk)

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

    // 상태 변경 메서드 추가
    public void changeRentalState(String newState) {
        this.rentalState = newState;
    }

    // 운송장번호, 운송업체명 변경 메서드 추가
    public void changeDelivery(String newDeliveryNo, String newDeliverCom) {
        this.deliveryNo = newDeliveryNo;
        this.deliverCom = newDeliverCom;
    }

    public void changeDestinationNo(int newDestinationNo) {
        this.destinationNo = newDestinationNo;
    }

    public void changeRentalPeriod(LocalDateTime startDate, int rentalTerm) {
        if (startDate == null || rentalTerm < 0) {
            throw new IllegalArgumentException("잘못된 대여 기간 설정입니다.");
        }
        this.rentalStartDate = startDate;
        this.rentalEndDate = startDate.plusMonths(rentalTerm);
    }

    @Transient
    private String rentalPrice;

    @Transient
    private int rentalTerm;

    @Transient
    private String productName;

    @Transient
    private String productImageLink;

}
