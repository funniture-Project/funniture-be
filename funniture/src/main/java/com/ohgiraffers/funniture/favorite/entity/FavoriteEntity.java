package com.ohgiraffers.funniture.favorite.entity;

import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_like")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FavoriteCombinedKey.class)
public class FavoriteEntity {

    @Id
    @Column(name = "member_id")
    private String memberId;

    @Id
    @Column(name = "product_no")
    private String productNo;  // String 타입으로 변경

    @Transient
    private String productName;

    @Transient
    private String productStatus;

    @Transient
    private String productImageLink;

    @Transient
    private String storeName;

    @Transient
    private String categoryName;

    @Transient
    private String priceList;

    public List<Integer> getPriceListAsIntegers() {
        if (priceList == null || priceList.isEmpty()) {
            return List.of();
        }
        return Arrays.stream(priceList.split(" "))
                .map(Integer::parseInt)     // 정수 변환
                .collect(Collectors.toList());
    }
}
