package com.ohgiraffers.funniture.rental.entity;

import com.ohgiraffers.funniture.deliveryaddress.entity.DeliveryAddressEntity;
import com.ohgiraffers.funniture.member.entity.OwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.ProductEntity;
import com.ohgiraffers.funniture.product.entity.RentalOptionInfoEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name="tbl_rental")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class DetailRentalEntity {

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

    @ManyToOne
    @JoinColumn(name = "owner_no", referencedColumnName = "member_id")
    private OwnerInfoEntity ownerInfoEntity;         // 제공자 회원번호 (fk)

    @OneToOne
    @JoinColumn(name = "rental_info_no")
    private RentalOptionInfoEntity rentalOptionInfoEntity;       // 대여조건정보 (fk)

    @OneToOne
    @JoinColumn(name = "destination_no")
    private DeliveryAddressEntity deliveryAddressEntity;      // 배송지 식별번호 (fk)

    @ManyToOne
    @JoinColumn(name = "product_no")
    private ProductEntity productEntity;       // 상품번호 (fk)
}
