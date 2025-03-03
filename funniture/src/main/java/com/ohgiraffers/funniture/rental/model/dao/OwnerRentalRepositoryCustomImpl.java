package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.deliveryaddress.entity.QDeliveryAddressEntity;
import com.ohgiraffers.funniture.member.entity.QMemberEntity;
import com.ohgiraffers.funniture.member.entity.QOwnerInfoEntity;
import com.ohgiraffers.funniture.product.entity.QProductEntity;
import com.ohgiraffers.funniture.product.entity.QRentalOptionInfoEntity;
import com.ohgiraffers.funniture.rental.entity.QDetailRentalEntity;
import com.ohgiraffers.funniture.rental.entity.QUserRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.OwnerRentalViewDTO;
import com.ohgiraffers.funniture.rental.model.dto.RentalDetailDTO;
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
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerRentalRepositoryCustomImpl implements OwnerRentalRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<OwnerRentalViewDTO> findRentalListByOwner(String ownerNo, String period, String rentalTab, Pageable pageable) {

        // 현재 날짜
        LocalDate currentDate = LocalDate.now();  // 현재 날짜를 사용

        // User 쪽과 조인이 같아서 재사용!
        QOwnerInfoEntity ownerInfo = QOwnerInfoEntity.ownerInfoEntity;
        QRentalOptionInfoEntity rentalOptionInfo = QRentalOptionInfoEntity.rentalOptionInfoEntity;
        QDeliveryAddressEntity deliveryAddress = QDeliveryAddressEntity.deliveryAddressEntity;
        QProductEntity product = QProductEntity.productEntity;
        QDetailRentalEntity rental = QDetailRentalEntity.detailRentalEntity;
        QMemberEntity member = QMemberEntity.memberEntity;

        // 기본적으로 전체 데이터를 조회할 때는 기간 필터를 추가하지 않음
        BooleanBuilder whereCondition = new BooleanBuilder();
        whereCondition.and(rental.ownerInfoEntity.memberId.eq(ownerNo));

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

        // **예약 진행 상태별 필터 추가**
        if (rentalTab != null) {
            if ("예약".equals(rentalTab)) {
                whereCondition.and(rental.rentalState.in("예약대기", "예약완료", "예약취소"));
            } else if ("배송".equals(rentalTab)) {
                whereCondition.and(rental.rentalState.in("배송중", "배송완료"));
            } else if ("반납".equals(rentalTab)) {
                whereCondition.and(rental.rentalState.in("반납요청", "수거중", "반납완료"));
            }
        }

        // QueryDSL을 사용하여 쿼리 실행
        JPAQuery<OwnerRentalViewDTO> query = jpaQueryFactory
                .select(Projections.constructor(OwnerRentalViewDTO.class,
                        rental.rentalNo,
                        rental.orderDate,
                        rental.rentalNumber,
                        rental.rentalState,
                        rental.deliveryMemo,
                        ownerInfo.storeName,
                        product.productName,
                        rentalOptionInfo.rentalPrice,
                        rentalOptionInfo.rentalTerm,
                        rentalOptionInfo.asNumber,
                        deliveryAddress.destinationName,
                        deliveryAddress.destinationPhone,
                        deliveryAddress.destinationAddress,
                        deliveryAddress.receiver,
                        member.email,
                        member.userName,
                        member.phoneNumber,
                        rental.deliverCom,
                        rental.deliveryNo,
                        rental.rentalStartDate,
                        rental.rentalEndDate
                ))
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.ownerInfoEntity, ownerInfo)
                .join(rental.rentalOptionInfoEntity, rentalOptionInfo)
                .join(rental.deliveryAddressEntity, deliveryAddress)
                .join(rental.memberEntity, member)
                .where(whereCondition)
                .orderBy(rental.orderDate.desc())
                .offset(pageable.getOffset()) // 페이지 오프셋 설정
                .limit(pageable.getPageSize()); // 페이지 크기 설정

        List<OwnerRentalViewDTO> ownerRentalList = query.fetch();

        // 전체 개수를 구하기 위해 limit과 offset을 제외한 count query 실행
        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(rental.count())
                .from(rental)
                .join(rental.productEntity, product)
                .join(rental.ownerInfoEntity, ownerInfo)
                .join(rental.rentalOptionInfoEntity, rentalOptionInfo)
                .join(rental.deliveryAddressEntity, deliveryAddress)
                .join(rental.memberEntity, member)
                .where(whereCondition);

        Long countResult = countQuery.fetchOne();
        long totalCount = (countResult != null) ? countResult : 0;

        return new PageImpl<>(ownerRentalList, pageable, totalCount);  // Page 객체 반환
    }
}
