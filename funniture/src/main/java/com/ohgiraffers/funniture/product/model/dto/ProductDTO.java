package com.ohgiraffers.funniture.product.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    private String productNo;
    private String productName;
    private String ownerNo;
    private int totalStock;
    private Integer usedStock;

    private int categoryCode;

    // 정산 판매가
    private int regularPrice;

    // 상품 설명
    private String productContent;

    // 판매 상태
    private String productStatus;

    // 대표이미지 링크
    private String productImageLink;

    // 대표이미지 ID
    private String productImageId;
}
