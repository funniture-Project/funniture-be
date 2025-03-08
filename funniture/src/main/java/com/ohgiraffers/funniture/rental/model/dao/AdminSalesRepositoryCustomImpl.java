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
    public Page<AdminSalesDTO> findSalesByDate(String yearMonth, String day, Pageable pageable) {
        QAdminSalesEntity rental = QAdminSalesEntity.adminSalesEntity;
        QRentalOptionInfoEntity rentalOptionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QAdminProductEntity product = QAdminProductEntity.adminProductEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QAdminCategoryEntity category = QAdminCategoryEntity.adminCategoryEntity;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(rental.orderDate.stringValue().like(yearMonth + "%"));

        if (day != null && !day.isEmpty()) {
            whereClause.and(rental.orderDate.stringValue().eq(yearMonth + "-" + day));
        }

        whereClause.and(rental.rentalState.in("예약완료", "배송중", "배송완료", "반납요청", "수거중", "반납완료"));

        // 날짜 포맷 추가
        StringTemplate formattedOrderDate = Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", rental.orderDate);

        JPAQuery<AdminSalesDTO> query = jpaQueryFactory
                .select(Projections.constructor(AdminSalesDTO.class,
                        product.productNo,
                        product.productName,
                        category.categoryName,
                        ownerInfo.storeName,
                        rentalOptionInfo.rentalPrice.multiply(rental.rentalNumber).sum().as("totalAmount"),
                        formattedOrderDate.as("orderDate")  // 날짜 형식 수정
                ))
                .from(rental)
                .join(rental.rentalOptionInfo, rentalOptionInfo)
                .join(rental.adminProduct, product)
                .join(product.adminCategory, category)
                .join(product.adminOwnerInfo, ownerInfo)
                .where(whereClause)
                .groupBy(product.productNo, product.productName, category.categoryName, ownerInfo.storeName, formattedOrderDate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<AdminSalesDTO> salesData = query.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(rental.count())
                .from(rental)
                .join(rental.rentalOptionInfo, rentalOptionInfo)
                .join(rental.adminProduct, product)
                .join(product.adminCategory, category)
                .join(product.adminOwnerInfo, ownerInfo)
                .where(whereClause);

        Long countResult = countQuery.fetchOne();
        long totalCount = (countResult != null) ? countResult : 0;

        return new PageImpl<>(salesData, pageable, totalCount);

    }
}
