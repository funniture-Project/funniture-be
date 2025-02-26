package com.ohgiraffers.funniture.favorite.model.dto;

import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FavoriteDTO {

    private String memberId;
    private String productNo;
    private String productName;
    private String productStatus;
    private String productImageLink;
    private String storeName;
    private String categoryName;
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
