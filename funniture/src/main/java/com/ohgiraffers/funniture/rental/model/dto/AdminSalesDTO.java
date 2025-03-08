package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminSalesDTO {

    private String productNo;

    private String productName;

    private String categoryName;

    private String storeName;

    private Integer totalAmount;  // 매출

    private String orderDate;   // 주문일


}
