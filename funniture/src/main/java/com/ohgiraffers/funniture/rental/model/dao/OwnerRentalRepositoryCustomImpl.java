package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QUserProductEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.OwnerRentalViewDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerRentalRepositoryCustomImpl implements OwnerRentalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<OwnerRentalViewDTO> findRentalListByOwner(String ownerNo, String period) {

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();  // 현재 날짜를 사용

        // User 쪽과 조인이 같아서 재사용!
        QUserRentalEntity rental = QUserRentalEntity.userRentalEntity;
        QUserProductEntity product = QUserProductEntity.userProductEntity;
        QRentalOptionInfoEntity optionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;

        // 기본적으로 전체 데이터를 조회할 때는 기간 필터를 추가하지 않음
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(rental.ownerNo.eq(ownerNo));

        // 기간에 맞는 조건을 추가
        if (period != null) {
            LocalDateTime startDate = null;
            LocalDateTime endDate = LocalDateTime.now(); // 기본적으로 현재 날짜로 설정

            // 기간 버튼에 따라 날짜 범위 설정
            if ("1WEEK".equals(period)) {
                startDate = currentDate.minusWeeks(1).atStartOfDay();
            } else if ("1MONTH".equals(period)) {
                startDate = currentDate.minusMonths(1).atStartOfDay();
            } else if ("3MONTH".equals(period)) {
                startDate = currentDate.minusMonths(3).atStartOfDay();
            }

            // 만약 startDate가 설정되었다면, 해당 기간에 맞는 필터링을 적용
            if (startDate != null) {
                whereCondition.and(rental.rentalEndDate.between(startDate, endDate));
            }
        }

        // QueryDSL을 사용하여 쿼리 실행
        JPAQuery<OwnerRentalViewDTO> query = jpaQueryFactory
                .select(Projections.constructor(OwnerRentalViewDTO.class,
                        rental.rentalNo,
                        rental.deliverCom,
                        rental.deliveryNo,
                        rental.rentalNumber,
                        rental.rentalStartDate,
                        rental.rentalEndDate,
                        rental.rentalState,
                        product.productName,
                        optionInfo.rentalTerm,
                        optionInfo.asNumber
                ))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, optionInfo)
                .where(whereCondition);

        return query.fetch();
    }
}
