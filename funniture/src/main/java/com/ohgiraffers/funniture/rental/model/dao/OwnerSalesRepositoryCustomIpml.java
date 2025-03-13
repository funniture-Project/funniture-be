package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.member.entity.QMemberEntity;
import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.OwnerSalesDTO;
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
public class OwnerSalesRepositoryCustomIpml implements OwnerSalesRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OwnerSalesDTO> getSalesByOwner(String ownerNo, String yearMonth, String productNo) {
        QUserRentalEntity rental = QUserRentalEntity.userRentalEntity;
        QProductEntity product = QProductEntity.productEntity;
        QRentalOptionInfoEntity rentalOptionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        // YearMonth 파싱 (예: "2025-03" -> YearMonth 객체)
        YearMonth targetMonth = YearMonth.parse(yearMonth);
        LocalDateTime startDate = targetMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = targetMonth.atEndOfMonth().atTime(23, 59, 59);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(rental.ownerNo.eq(ownerNo))
                .and(rental.orderDate.between(startDate, endDate));

        builder.and(rental.rentalState.in("예약완료", "배송중", "배송완료", "반납요청", "수거중", "반납완료"));

        // productNo 조건 추가 (비동기 필터링 대비)
        if(productNo != null && !productNo.isEmpty()) {
            builder.and(product.productNo.eq(productNo));
        }

        return jpaQueryFactory
                .select(Projections.constructor(
                        OwnerSalesDTO.class,
                        rental.rentalNo,
                        member.userName,
                        rental.orderDate,
                        rental.rentalStartDate,
                        rental.rentalEndDate,
                        rental.rentalNumber,
                        product.productNo,
                        product.productName,
                        rentalOptionInfo.rentalPrice
                ))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, rentalOptionInfo)
                .join(member).on(rental.memberId.eq(member.memberId))
                .where(builder)
                .orderBy(rental.rentalNo.asc())
                .fetch();
    }
}
