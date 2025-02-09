package com.ohgiraffers.funniture.rental.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="tbl_rentaloptioninfo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class UserRentalOptionInfoEntity {

    @Id
    @Column(name = "rental_info_no")
    private int rentalInfoNo;

    @Column(name = "product_no")
    private String productNo;

    @Column(name = "rental_term")
    private int rentalTerm;

    @Column(name = "rental_price")
    private int rentalPrice;

    @Column(name = "as_number")
    private int asNumber;

}
