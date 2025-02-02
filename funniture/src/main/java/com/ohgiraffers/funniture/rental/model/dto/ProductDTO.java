package com.ohgiraffers.funniture.rental.model.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    private String productNo;        // 상품번호

    private String productName;      // 상품명

    private String ownerNo;          // 제공자 회원번호

    private int totalStock;          // 전체 재고

    private int usedStock;           // 사용 재고

    private int categoryCode;        // 카테고리 코드

    private int regularPrice;        // 정산 판매가

    private String productContent;   // 상품 설명

    private String productStatus;    // 판매 상태

    private String productImageLink; // 대표이미지 링크

    private String productImageId;   // 대표이미지 ID

}
