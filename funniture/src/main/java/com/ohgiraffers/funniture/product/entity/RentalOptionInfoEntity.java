package com.ohgiraffers.funniture.product.entity;

import com.ohgiraffers.funniture.product.model.dto.RentalOptionInfoDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tbl_rentaloptioninfo")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class RentalOptionInfoEntity {

    @Id
    @Column(name = "rental_info_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rentalInfoNo;

    @Column(name = "product_no")
    private String productNo;

    @Column(name = "rental_term")
    private int rentalTerm;

    @Column(name = "rental_price")
    private int rentalPrice;

    @Column(name = "as_number")
    private Integer asNumber;

    @Column(name = "is_active")
    private Boolean isActive;

    // update 메서드 추가
    public void update(RentalOptionInfoDTO dto) {
        this.rentalTerm = dto.getRentalTerm();
        this.rentalPrice = dto.getRentalPrice();
        this.asNumber = dto.getAsNumber();
        this.isActive = dto.isActive();  // isActive도 업데이트
    }
}
