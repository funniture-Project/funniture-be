package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.common.Criteria;
import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.UserOrderViewDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRentalRepositoryCustomImpl implements UserRentalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserOrderViewDTO> findRentalOrderListByUser(String memberId, String period, LocalDate searchDate, Pageable pageable) {
        QUserRentalEntity rental = QUserRentalEntity.userRentalEntity;
        QProductEntity product = QProductEntity.productEntity;
        QRentalOptionInfoEntity optionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(rental.memberId.eq(memberId));

        if ("1MONTH".equals(period)) {
            searchDate = LocalDate.now().minusMonths(1); // 1개월 전 날짜
        } else if ("3MONTH".equals(period)) {
            searchDate = LocalDate.now().minusMonths(3); // 3개월 전 날짜
        }

        // 검색 날짜 필터 적용
        if (searchDate != null) {
            LocalDateTime startDate = searchDate.atStartOfDay();  // 시작 시각 00:00:00
            LocalDateTime endDate;

            // 사용자가 직접 날짜를 선택한 경우 → 해당 날짜만 조회
            if (period == null) {
                endDate = searchDate.atTime(23, 59, 59); // 선택한 날짜 하루만 조회
            } else {
                endDate = LocalDateTime.now();
            }

            whereCondition.and(rental.orderDate.between(startDate, endDate));
        }

        JPAQuery<UserOrderViewDTO> query = jpaQueryFactory
                .select(Projections.constructor(UserOrderViewDTO.class,
                        rental.rentalNo,
                        rental.orderDate,
                        rental.rentalState,
                        rental.rentalNumber,
                        product.productName,
                        product.productNo,
                        optionInfo.rentalPrice))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, optionInfo)
                .where(whereCondition)
                .orderBy(rental.orderDate.desc())
                .offset(pageable.getOffset()) // 페이지 오프셋 설정
                .limit(pageable.getPageSize()); // 페이지 크기 설정;

        List<UserOrderViewDTO> userOrderList = query.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(rental.count())
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, optionInfo)
                .where(whereCondition);

        Long countResult = countQuery.fetchOne();
        long totalCount = (countResult != null) ? countResult : 0;

        return new PageImpl<>(userOrderList, pageable, totalCount);  // Page 객체 반환
    }
}
