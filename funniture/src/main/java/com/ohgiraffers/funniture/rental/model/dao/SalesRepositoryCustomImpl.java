package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminCategoryEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminProductEntity;
import com.ohgiraffers.funniture.rental.entity.QAdminSalesEntity;
import com.ohgiraffers.funniture.rental.model.dto.AdminMonthlySalesDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SalesRepositoryCustomImpl implements SalesRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<AdminMonthlySalesDTO> getSales(String yearMonth, String groupBy) {
        QAdminSalesEntity rental = QAdminSalesEntity.adminSalesEntity;
        QRentalOptionInfoEntity rentalOptionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QAdminProductEntity product = QAdminProductEntity.adminProductEntity;
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QAdminCategoryEntity category = QAdminCategoryEntity.adminCategoryEntity;

        BooleanBuilder whereClause = new BooleanBuilder();

        // 날짜 포맷을 YYYY-MM 또는 YYYY-MM-DD로 그룹화
        if ("month".equals(groupBy)) {
            whereClause.and(rental.orderDate.stringValue().like(yearMonth + "%"));
        } else if ("day".equals(groupBy)) {
            whereClause.and(rental.orderDate.stringValue().like(yearMonth + "%"));
        }

        // 주문 상태 필터링
        whereClause.and(rental.rentalState.in("예약완료", "배송중", "배송완료", "반납요청", "수거중", "반납완료"));

        // 쿼리 실행
        return jpaQueryFactory
                .select(Projections.constructor(AdminMonthlySalesDTO.class,
                        rentalOptionInfo.rentalPrice.multiply(rental.rentalNumber).sum().as("totalAmount"),
                        // groupBy 부분에서 날짜 포맷 처리
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", rental.orderDate).as("groupBy")  // 월별 그룹화
                ))
                .from(rental)
                .join(rental.rentalOptionInfo, rentalOptionInfo)
                .join(rental.adminProduct, product)
                .join(product.adminCategory, category)
                .join(product.adminOwnerInfo, ownerInfo)
                .where(whereClause)
                .groupBy(Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m')", rental.orderDate))  // 월별 그룹화
                .fetch();  // 여기서 데이터를 실제로 가져옴
    }
}
