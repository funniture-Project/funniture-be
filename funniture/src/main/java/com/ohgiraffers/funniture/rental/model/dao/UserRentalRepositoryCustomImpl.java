package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRentalRepositoryCustomImpl implements UserRentalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<UserOrderViewDTO> findRentalOrderListByUser(String memberId, String period, LocalDate searchDate) {
        QUserRentalEntity rental = QUserRentalEntity.userRentalEntity;
        QProductEntity product = QProductEntity.productEntity;
        QRentalOptionInfoEntity optionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;

        JPAQuery<UserOrderViewDTO>query = jpaQueryFactory
                .select(Projections.constructor(UserOrderViewDTO.class,
                        rental.rentalNo,
                        rental.orderDate,
                        rental.rentalState,
                        product.productName,
                        optionInfo.rentalPrice))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, optionInfo)
                .where(rental.memberId.eq(memberId));

        if ("1MONTH".equals(period)) {
            searchDate = LocalDate.now().minusMonths(1); // 1개월 전 날짜
        } else if ("3MONTH".equals(period)) {
            searchDate = LocalDate.now().minusMonths(3); // 3개월 전 날짜
        }

        // 검색 날짜 필터 적용
        if (searchDate != null) {
            LocalDateTime startOfDay = searchDate.atStartOfDay();  // 시작 시각 00:00:00
            LocalDateTime endOfDay;

            // 사용자가 직접 날짜를 선택한 경우 → 해당 날짜만 조회
            if (period == null) {
                endOfDay = searchDate.atTime(23, 59, 59); // 선택한 날짜 하루만 조회
            } else {
                endOfDay = LocalDateTime.now(); // 1개월/3개월 버튼 클릭 시, 현재 날짜까지 조회
            }

            query.where(rental.orderDate.between(startOfDay, endOfDay));
        }

        return query.fetch();
    }
}
