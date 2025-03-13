package com.ohgiraffers.funniture.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductWithPriceEntity {

    @Id
    @Column(name = "product_no")
    private String productNo;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "owner_no")
    private String ownerNo;

    @Column(name = "total_stock")
    private int totalStock;

    @Column(name = "used_stock")
    private int usedStock;

    @Column(name = "category_code")
    private Integer categoryCode;

    // 정산 판매가
    @Column(name = "regular_price")
    private int regularPrice;

    // 상품 설명
    @Column(name = "product_content", columnDefinition = "MEDIUMTEXT")
    private String productContent;

    // 판매 상태
    @Column(name = "product_status")
    private String productStatus;

    // 대표 이미지 링크
    @Column(name = "product_image_link")
    private String productImageLink;

    // 대표 이미지 ID(삭제 시 필요)
    @Column(name = "product_image_id")
    private String productImageId;

    @Column(name = "regist_date")
    private LocalDateTime registerData;

    @Transient
    private String storeName;

    @Transient
    private String priceList;

    public List<Integer> getPriceListAsIntegers() {
        if (priceList == null || priceList.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(priceList.split(" ")) // 쉼표로 분할
                .map(Integer::parseInt)     // 정수 변환
                .collect(Collectors.toList());
    }

//    public static ProductWithPriceDTO fromQueryResult(Object[] result) {
//        return new ProductWithPriceDTO(
//                (String) result[0],  // product_no
//                (String) result[1],  // product_name
//                (String) result[2],  // owner_no
//                (int) result[3],     // total_stock
//                (Integer) result[4], // used_stock
//                (int) result[5],     // category_code
//                (int) result[6],     // regular_price
//                (String) result[7],  // product_content
//                (String) result[8],  // product_status
//                (String) result[9],  // productImageLink
//                (String) result[10], // productImageId
//                (String) result[11]  // priceList
//        );
//    }
}
