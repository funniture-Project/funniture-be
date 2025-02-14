package com.ohgiraffers.funniture.product.model.dto;

import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    private String storeName;

    // 가격 리스트
    private String priceList;

    public List<Integer> getPriceListAsIntegers() {
        if (priceList == null || priceList.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(priceList.split(",")) // 쉼표로 분할
                .map(Integer::parseInt)     // 정수 변환
                .collect(Collectors.toList());
    }
}
