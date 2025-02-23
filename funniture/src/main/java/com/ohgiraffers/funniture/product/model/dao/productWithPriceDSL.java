package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class productWithPriceDSL implements ProductWithPriceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ProductWithPriceEntity> findSearchProductList(ProductSearchCondition condition) {

        System.out.println("진행 할 condition = " + condition);

        QProductWithPriceEntity productPrice = QProductWithPriceEntity.productWithPriceEntity;
        QRentalOptionInfoEntity rentalOption = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QCategoryEntity category = QCategoryEntity.categoryEntity;

        BooleanBuilder builder = new BooleanBuilder();

        // 카테고리 정보
        if (condition.getCategoryCode() != null){
            if (condition.getCategoryCode().contains(1) || condition.getCategoryCode().contains(2)){
                builder.and(category.refCategoryCode.in(condition.getCategoryCode()));
            } else {
                builder.and(productPrice.categoryCode.in(condition.getCategoryCode()));
            }
        }

        // 검색어
        if (condition.getSearchText() != null){
            builder.and(productPrice.productName.contains(condition.getSearchText()));
        }

        // 판매상태
        if (condition.getProductStatus() != null){
            builder.and(productPrice.productStatus.eq(condition.getProductStatus()));
        }

        // 제공사 정보
        if (condition.getOwnerNo() != null){
            builder.and(productPrice.ownerNo.in(condition.getOwnerNo()));
        }

        return jpaQueryFactory
                .select(Projections.bean(ProductWithPriceEntity.class,
                        productPrice.productNo,
                        productPrice.productName,
                        productPrice.ownerNo,
                        productPrice.totalStock,
                        productPrice.usedStock,
                        productPrice.categoryCode,
                        productPrice.regularPrice,
                        productPrice.productContent,
                        productPrice.productStatus,
                        productPrice.productImageLink,
                        productPrice.productImageId,
                        ownerInfo.storeName,
                        // rentalOption의 rentalPrice를 여러 값으로 이어붙이기
                        Expressions.stringTemplate("GROUP_CONCAT({0}) ", rentalOption.rentalPrice).as("priceList")))  // GROUP_CONCAT 사용List"))) // stringTemplate 사용
                .from(productPrice)
                .leftJoin(rentalOption).on(productPrice.productNo.eq(rentalOption.productNo))
                .leftJoin(ownerInfo).on(productPrice.ownerNo.eq(ownerInfo.memberId))
                .leftJoin(category).on(productPrice.categoryCode.eq(category.categoryCode))
                .where(builder)
                .groupBy(productPrice.productNo)
                .fetch();
    }
}
