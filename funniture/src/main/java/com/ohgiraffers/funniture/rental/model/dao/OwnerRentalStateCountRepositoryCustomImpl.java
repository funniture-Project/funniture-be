package com.ohgiraffers.funniture.rental.model.dao;

import com.ohgiraffers.funniture.rental.entity.QRentalEntity;
import com.ohgiraffers.funniture.rental.model.dto.RentalStateCountDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnerRentalStateCountRepositoryCustomImpl implements OwnerRentalStateCountRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<RentalStateCountDTO> countRentalStatesByOwner(String ownerNo) {
        QRentalEntity rental = QRentalEntity.rentalEntity;

        List<RentalStateCountDTO> results = jpaQueryFactory
                .select(Projections.constructor(RentalStateCountDTO.class,
                        rental.rentalState,
                        rental.count()))
                .from(rental)
                .where(rental.ownerNo.eq(ownerNo)
                        .and(rental.rentalState.in("예약대기", "예약완료", "예약취소")))
                .groupBy(rental.rentalState)  // 상태별 개수 그룹화
                .fetch();

        return results;
    }
}
