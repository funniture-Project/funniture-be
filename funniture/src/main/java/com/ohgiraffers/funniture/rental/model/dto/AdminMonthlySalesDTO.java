package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AdminMonthlySalesDTO {

    private Integer totalAmount;  // 매출 합계

    private String groupBy;  // 월별/일별 (YYYY-MM, YYYY-MM-DD)

}
