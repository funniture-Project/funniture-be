package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MonthlySalesDTO {

    private String ownerNo;        // 제공자 회원번호

    private Integer totalSales;    // 월별 전체 매출 합계

}
