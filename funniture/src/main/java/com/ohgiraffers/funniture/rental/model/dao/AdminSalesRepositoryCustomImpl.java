package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminCategoryEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminProductEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminRentalEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminSalesEntity;
import com.ohgiraffers.funniture.rental.model.dto.AdminRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.AdminSalesDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AdminSalesRepositoryCustomImpl implements AdminSalesRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdminSalesDTO> findSalesByDate(String yearMonth, String storeName, Pageable pageable) {
        QAdminSalesEntity rental = QAdminSalesEntity.adminSalesEntity;
        QRentalOptionInfoEntity rentalOptionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QAdminProductEntity product = QAdminProductEntity.adminProductEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QAdminCategoryEntity category = QAdminCategoryEntity.adminCategoryEntity;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(rental.orderDate.stringValue().like(yearMonth + "%"));

        whereClause.and(rental.rentalState.in("예약완료", "배송중", "배송완료", "반납요청", "수거중", "반납완료"));

        if (storeName != null && !storeName.trim().isEmpty()) {
            // 한글 검색 가능
            whereClause.and(ownerInfo.storeName.contains(storeName)); // contains로 검색어 포함 조건 추가
        }

        // 날짜 포맷 추가
        StringTemplate formattedOrderDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", rental.orderDate);

        JPAQuery<AdminSalesDTO> query = jpaQueryFactory
                .select(Projections.constructor(AdminSalesDTO.class,
                        product.productNo,
                        product.productName,
                        category.categoryName,
                        ownerInfo.storeName,
                        rentalOptionInfo.rentalPrice.multiply(rental.rentalNumber).sum().as("totalAmount")
                ))
                .from(rental)
                .join(rental.rentalOptionInfo, rentalOptionInfo)
                .join(rental.adminProduct, product)
                .join(product.adminCategory, category)
                .join(product.adminOwnerInfo, ownerInfo)
                .where(whereClause)
                .groupBy(product.productNo, product.productName, category.categoryName, ownerInfo.storeName, formattedOrderDate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(product.productNo.asc());

        List<AdminSalesDTO> salesData = query.fetch();

        JPAQuery<Integer> countQuery = jpaQueryFactory
                .select(Expressions.ONE)  // 그룹화된 개수를 구할 땐 .select(Expressions.ONE) 사용
                .from(rental)
                .join(rental.rentalOptionInfo, rentalOptionInfo)
                .join(rental.adminProduct, product)
                .join(product.adminCategory, category)
                .join(product.adminOwnerInfo, ownerInfo)
                .where(whereClause)
                .groupBy(product.productNo, product.productName, category.categoryName, ownerInfo.storeName, formattedOrderDate);

        Integer countResult = countQuery.fetchOne();
        long totalCount = (countResult != null) ? countResult : 0;

        return new PageImpl<>(salesData, pageable, totalCount);

    }
}
