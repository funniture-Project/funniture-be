package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.ActiveRentalDTO;
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

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserActiveRentalRepositoryCustomImpl implements UserActiveRentalRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ActiveRentalDTO> findActiveRentalListByUser(String memberId, Pageable pageable) {

        QUserRentalEntity rental = QUserRentalEntity.userRentalEntity;
        QProductEntity product = QProductEntity.productEntity;
        QRentalOptionInfoEntity optionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;

        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(rental.memberId.eq(memberId));
        whereCondition.and(rental.rentalState.eq("배송완료"));

        JPAQuery<ActiveRentalDTO> query = jpaQueryFactory
                .select(Projections.constructor(ActiveRentalDTO.class,
                        rental.rentalNo,
                        rental.rentalNumber,
                        rental.rentalStartDate,
                        rental.rentalEndDate,
                        product.productName,
                        optionInfo.rentalTerm,
                        optionInfo.asNumber
                ))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, optionInfo)
                .where(whereCondition)
                .orderBy(rental.rentalEndDate.desc())
                .offset(pageable.getOffset()) // 페이지 오프셋 설정
                .limit(pageable.getPageSize()); // 페이지 크기 설정;

        List<ActiveRentalDTO> activeRentalList = query.fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(rental.count())
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.rentalOptionInfoEntity, optionInfo)
                .where(whereCondition);

        Long countResult = countQuery.fetchOne();
        long totalCount = (countResult != null) ? countResult : 0;

        return new PageImpl<>(activeRentalList, pageable, totalCount);  // Page 객체 반환

    }
}
