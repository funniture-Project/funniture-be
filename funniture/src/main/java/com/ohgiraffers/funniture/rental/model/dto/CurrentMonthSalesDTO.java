package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CurrentMonthSalesDTO {

    private String productNo;      // 상품 번호

    private String productName;    // 상품 이름

    private Integer totalSales;    // 상품별 매출 합계

}
