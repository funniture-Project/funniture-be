package com.ohgiraffers.funniture.product.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductDTO {

    private String productNo;
    private String productName;
    private String ownerNo;
    private int totalStock;
    private int usedStock;

    private CategoryDTO categoryCode;

    // 정산 판매가
    private int regularPrice;

    // 상품 설명
    private String productContent;

    // 판매 상태
    private String productStatus;

    // 이미지 존재 여부 (대표 이미지)
    private boolean hasImg;
}
