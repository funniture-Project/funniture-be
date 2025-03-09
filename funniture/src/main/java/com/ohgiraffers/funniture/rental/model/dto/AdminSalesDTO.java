package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

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


}
