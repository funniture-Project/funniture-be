package com.ohgiraffers.funniture.product.model.dao;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.common.OrderByNull;
import com.ohgiraffers.funniture.common.ProductSearchCondition;
import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.*;
import com.ohgiraffers.funniture.product.model.dto.RecentProductDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class productWithPriceDSL implements ProductWithPriceRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private List<ProductWithPriceEntity> common (ProductSearchCondition condition, Pageable pageable){
        System.out.println("DSL 진행 할 condition = " + condition);

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

        JPAQuery<ProductWithPriceEntity> query =  jpaQueryFactory
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
//                        Expressions.stringTemplate("GROUP_CONCAT({0}) ", rentalOption.rentalPrice).as("priceList")))  // GROUP_CONCAT 사용List"))) // stringTemplate 사용
                        Expressions.stringTemplate(
                                "GROUP_CONCAT(CASE WHEN {0} THEN {1} ELSE NULL END)",
                                rentalOption.isActive.isTrue(),
                                rentalOption.rentalPrice
                        ).as("priceList")))
                .from(productPrice)
                .leftJoin(rentalOption).on(productPrice.productNo.eq(rentalOption.productNo))
                .leftJoin(ownerInfo).on(productPrice.ownerNo.eq(ownerInfo.memberId))
                .leftJoin(category).on(productPrice.categoryCode.eq(category.categoryCode))
                .where(builder)
                .groupBy(productPrice.productNo);

        if (pageable != null){
            query.offset(pageable.getOffset());
            query.limit(pageable.getPageSize());
        }

        return query.fetch();
    }

    @Override
    public Page<ProductWithPriceEntity> findSearchPagingProductList(ProductSearchCondition condition, Criteria cri) {
        QProductWithPriceEntity productPrice = QProductWithPriceEntity.productWithPriceEntity;
        QRentalOptionInfoEntity rentalOption = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QCategoryEntity category = QCategoryEntity.categoryEntity;

        // 전체 개수를 구하기 위해 limit과 offset을 제외한 count query 실행
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

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(productPrice.productNo.countDistinct())
                .from(productPrice)
                .leftJoin(rentalOption).on(productPrice.productNo.eq(rentalOption.productNo))
                .leftJoin(ownerInfo).on(productPrice.ownerNo.eq(ownerInfo.memberId))
                .leftJoin(category).on(productPrice.categoryCode.eq(category.categoryCode))
                .where(builder);

        Long countResult = countQuery.fetchOne();
        long totalCount = (countResult != null) ? countResult : 0;

        System.out.println("전체 페이지!!!!!!!!!!!!!!!!!!!!!! totalCount = " + totalCount);

        Pageable pageable = PageRequest.of(cri.getPageNum() - 1, cri.getAmount());  // 페이지 번호 조정

        List<ProductWithPriceEntity> result = common(condition,pageable);

        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public List<ProductWithPriceEntity> findSearchProductList(ProductSearchCondition condition) {
        List<ProductWithPriceEntity> result = common(condition, null);

        return result;
    }


    @Override
    public List<RecentProductDTO> findAllProductInfo(List<String> productList) {
        System.out.println("꺼내야할 정보 productList = " + productList);

        QProductWithPriceEntity productPrice = QProductWithPriceEntity.productWithPriceEntity;
        QRentalOptionInfoEntity rentalOption = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QCategoryEntity category = QCategoryEntity.categoryEntity;

        BooleanBuilder builder = new BooleanBuilder();

        // 상품 번호
        if (productList != null){
           builder.and(productPrice.productNo.in(productList));
        }

        return jpaQueryFactory
                .select(Projections.bean(RecentProductDTO.class,
                        productPrice.productNo,
                        productPrice.productName,
                        productPrice.categoryCode,
                        productPrice.productStatus,
                        productPrice.productImageLink,
                        ownerInfo.storeName,
                        // rentalOption의 rentalPrice를 여러 값으로 이어붙이기
//                        Expressions.stringTemplate("GROUP_CONCAT({0}) ", rentalOption.rentalPrice).as("priceList")))  // GROUP_CONCAT 사용List"))) // stringTemplate 사용
                        Expressions.stringTemplate(
                                "GROUP_CONCAT(CASE WHEN {0} THEN {1} ELSE NULL END)",
                                rentalOption.isActive.isTrue(),
                                rentalOption.rentalPrice
                        ).as("priceList")))
                .from(productPrice)
                .leftJoin(rentalOption).on(productPrice.productNo.eq(rentalOption.productNo))
                .leftJoin(ownerInfo).on(productPrice.ownerNo.eq(ownerInfo.memberId))
                .leftJoin(category).on(productPrice.categoryCode.eq(category.categoryCode))
                .where(builder)
                .groupBy(productPrice.productNo)
                .orderBy(OrderByNull.DEFAULT)
                .fetch();
    }
}
