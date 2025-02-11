package com.ohgiraffers.funniture.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "tbl_rentaloptioninfo")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
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
}
