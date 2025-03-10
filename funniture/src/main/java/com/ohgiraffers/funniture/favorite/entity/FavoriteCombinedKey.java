package com.ohgiraffers.funniture.favorite.entity;

import com.ohgiraffers.funniture.product.entity.ProductWithPriceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCombinedKey implements Serializable {

    private String memberId;
    private String productNo;
}
