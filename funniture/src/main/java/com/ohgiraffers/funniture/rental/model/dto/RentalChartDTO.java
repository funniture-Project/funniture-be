package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RentalChartDTO {

    private String date;        // 날짜 (YYYY-MM-DD 형식)
    private Long currentYearSales;  // 올해 총 매출 금액
    private Long lastYearSales;     // 작년 총 매출 금액

}
