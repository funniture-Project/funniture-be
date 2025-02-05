package com.ohgiraffers.funniture.product.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductWithPriceDTO {

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

    // 가격 리스트
//    private List<Integer> priceList;
    private String priceList;

    public static ProductWithPriceDTO fromQueryResult(Object[] result) {
        return new ProductWithPriceDTO(
                (String) result[0],  // product_no
                (String) result[1],  // product_name
                (String) result[2],  // owner_no
                (int) result[3],     // total_stock
                (Integer) result[4], // used_stock
                (int) result[5],     // category_code
                (int) result[6],     // regular_price
                (String) result[7],  // product_content
                (String) result[8],  // product_status
                (String) result[9],  // productImageLink
                (String) result[10], // productImageId
                (String) result[11]  // priceList
        );
    }
}
