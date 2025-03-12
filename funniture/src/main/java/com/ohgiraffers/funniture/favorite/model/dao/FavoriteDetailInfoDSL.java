package com.ohgiraffers.funniture.favorite.model.dao;

import com.ohgiraffers.funniture.favorite.entity.FavoriteEntity;
import com.ohgiraffers.funniture.favorite.entity.QFavoriteEntity;
import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.QCategoryEntity;
import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FavoriteDetailInfoDSL implements FavoriteDetailInfoRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<FavoriteEntity> findAllByMemberId(String memberId) {

        QFavoriteEntity favoriteEntity = QFavoriteEntity.favoriteEntity;
        QProductEntity product = QProductEntity.productEntity;
        QRentalOptionInfoEntity rentalOption = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QCategoryEntity category = QCategoryEntity.categoryEntity;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(favoriteEntity.memberId.eq(memberId));

        return jpaQueryFactory
                .select(Projections.bean(FavoriteEntity.class,
                        favoriteEntity.memberId,
                        favoriteEntity.productNo,
                        product.productStatus,
                        product.productImageLink,
                        product.productName,
                        ownerInfo.storeName,
                        category.categoryName,
                        Expressions.stringTemplate(
                                "GROUP_CONCAT(CASE WHEN {0} THEN {1} ELSE NULL END)",
                                rentalOption.isActive.isTrue(),
                                rentalOption.rentalPrice
                        ).as("priceList")))
                .from(favoriteEntity)
                .leftJoin(product).on(favoriteEntity.productNo.eq(product.productNo))
                .leftJoin(ownerInfo).on(product.ownerNo.eq(ownerInfo.memberId))
                .leftJoin(category).on(product.categoryCode.eq(category.categoryCode))
                .leftJoin(rentalOption).on(product.productNo.eq(rentalOption.productNo))
                .where(builder)
                .groupBy(favoriteEntity.memberId, favoriteEntity.productNo,
                        ownerInfo.storeName, category.categoryName)
                .fetch();
    }
}
