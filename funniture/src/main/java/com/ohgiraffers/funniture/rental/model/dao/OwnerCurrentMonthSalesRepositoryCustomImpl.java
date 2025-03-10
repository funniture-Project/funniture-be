package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.CurrentMonthSalesDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerCurrentMonthSalesRepositoryCustomImpl implements OwnerCurrentMonthSalesRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CurrentMonthSalesDTO> getCurrentMonthSales(String ownerNo, String yearMonth) {
        QUserRentalEntity rental = QUserRentalEntity.userRentalEntity;
        QProductEntity product = QProductEntity.productEntity;

        // YearMonth 파싱 (예: "2025-03" -> YearMonth 객체)
        YearMonth targetMonth = YearMonth.parse(yearMonth);
        LocalDateTime startDate = targetMonth.atDay(1).atStartOfDay(); // 월의 첫 날
        LocalDateTime endDate = targetMonth.atEndOfMonth().atTime(23, 59, 59); // 월의 마지막 날

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(rental.ownerNo.eq(ownerNo))  // 제공자 번호로 필터링
                .and(rental.orderDate.between(startDate, endDate));  // 해당 월 범위로 필터링

        builder.and(rental.rentalState.in("예약완료", "배송중", "배송완료", "반납요청", "수거중", "반납완료"));

        return jpaQueryFactory
                .select(Projections.constructor(
                        CurrentMonthSalesDTO.class,
                        product.productNo, // 상품번호
                        product.productName, // 상품명
                        rental.rentalOptionInfoEntity.rentalPrice.multiply(rental.rentalNumber).sum().as("totalSales") // 매출 합산
                ))
                .from(rental)
                .join(rental.productEntity, product)
                .where(builder)
                .groupBy(product.productNo, product.productName) // 상품별 그룹화
                .orderBy(product.productNo.asc()) // 상품번호 기준으로 정렬
                .fetch(); // 데이터 조회
    }
}
